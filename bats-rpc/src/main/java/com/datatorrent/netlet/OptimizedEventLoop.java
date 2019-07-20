/**
 * Copyright (C) 2015 DataTorrent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.netlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * OptimizedEventLoop class.</p>
 *
 * @since 1.1.0
 */
public class OptimizedEventLoop extends DefaultEventLoop
{
  private final static class SelectedSelectionKeySet extends AbstractSet<SelectionKey>
  {
    private SelectionKey[] keys;
    private int pos;

    private SelectedSelectionKeySet(int size)
    {
      pos = 0;
      keys = new SelectionKey[size];
    }

    public void forEach(final DefaultEventLoop defaultEventLoop)
    {
      while (defaultEventLoop.alive && pos > 0) {
        final SelectionKey sk = keys[--pos];
      keys[pos] = null;
        if (!sk.isValid()) {
          continue;
        }
        try {
          defaultEventLoop.handleSelectedKey(sk);
        } catch (Exception ex) {
          Listener l = (Listener)sk.attachment();
          if (l != null) {
            l.handleException(ex, defaultEventLoop);
          } else {
            logger.warn("Exception on unattached SelectionKey {} ", sk, ex);
          }
        }
      }
    }

    @Override
    public boolean add(SelectionKey key)
    {
      if (key == null) {
        return false;
      }
      keys[pos++] = key;
      if (pos == keys.length) {
        SelectionKey[] keys = new SelectionKey[this.keys.length << 1];
        System.arraycopy(this.keys, 0, keys, 0, pos);
        this.keys = keys;
      }
      return true;
    }

    @Override
    public int size()
    {
      return pos;
    }

    public boolean remove(Object o) {
      return false;
    }

    @Override
    public boolean contains(Object o) {
      if (o == null) {
        return false;
      }
      int i = pos;
      while (i > 0) {
        if (o.equals(keys[--i])) {
          return true;
        }
      }
      return false;
    }

    @Override
    public Iterator<SelectionKey> iterator() {
      throw new UnsupportedOperationException();
    }
  }

  @SuppressWarnings("deprecation")
  public OptimizedEventLoop(String id) throws IOException
  {
    super(id);
    try {
      ClassLoader systemClassLoader;
      if (System.getSecurityManager() == null) {
        systemClassLoader = ClassLoader.getSystemClassLoader();
      } else {
        systemClassLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
        {
          @Override
          public ClassLoader run()
          {
            return ClassLoader.getSystemClassLoader();
          }
        });
      }

      final Class<?> selectorClass = Class.forName("sun.nio.ch.SelectorImpl", false, systemClassLoader);
      if (selectorClass.isAssignableFrom(selector.getClass())) {
        Field selectedKeys = selectorClass.getDeclaredField("selectedKeys");
        Field publicSelectedKeys = selectorClass.getDeclaredField("publicSelectedKeys");
        selectedKeys.setAccessible(true);
        publicSelectedKeys.setAccessible(true);
        SelectedSelectionKeySet keys = new SelectedSelectionKeySet(1024);
        selectedKeys.set(selector, keys);
        publicSelectedKeys.set(selector, keys);
        logger.debug("Instrumented an optimized java.util.Set into: {}", selector);
      }
    }
    catch (Exception e) {
      logger.debug("Failed to instrument an optimized java.util.Set into: {}", selector, e);
    }
  }

  @SuppressWarnings({"SleepWhileInLoop", "ConstantConditions"})
  protected void runEventLoop()
  {
    Set<SelectionKey> selectedKeys = selector.selectedKeys();
    if (selectedKeys instanceof SelectedSelectionKeySet) {
      runEventLoop((SelectedSelectionKeySet) selectedKeys);
    } else
      super.runEventLoop();
  }

  private void runEventLoop(SelectedSelectionKeySet keys)
  {
    while (alive) {
      int size = tasks.size();
      try {
        if (size > 0) {
          Runnable task;
          while (alive && size > 0) {
            task = tasks.poll();
            if (task == null) {
              break;
            }
            try {
              task.run();
            } catch (RuntimeException e) {
              logger.error("Unexpected exception in task {}. Terminating {}.", task, this, e);
              disconnectAllKeysAndShutdown();
            }
            size--;
          }
          if (selector.selectNow() == 0) {
            continue;
          }
        } else if (selector.select() == 0) {
          continue;
        }
      } catch (IOException e) {
        logger.error("Unexpected exception in selector {}. Terminating {}.", selector, this, e);
        disconnectAllKeysAndShutdown();
      }
      keys.forEach(this);
    }
  }

  /*
   * TODO: move disconnectAllKeysAndShutdown() to DefaultEventLoop and make it protected.
   * Can't do this in the patch release without breaking semantic version.
   */
  private void disconnectAllKeysAndShutdown()
  {
    for (SelectionKey selectionKey : selector.keys()) {
      if (selectionKey.isValid()) {
        Channel channel = selectionKey.channel();
        if (channel != null && channel.isOpen()) {
          Listener l = (Listener)selectionKey.attachment();
          try {
            selectionKey.channel().close();
            if (l != null) {
              if (l instanceof Listener.ClientListener) {
                ((Listener.ClientListener)l).disconnected();
              }
              l.unregistered(selectionKey);
            }
          } catch (IOException e) {
            if (l != null) {
              l.handleException(e, this);
            } else {
              logger.warn("Exception while closing channel {} on unregistered key {}", channel, selectionKey, e);
            }
          }
        }
      }
    }
    alive = false;
    selector.wakeup();
  }

  private static final Logger logger = LoggerFactory.getLogger(OptimizedEventLoop.class);
}
