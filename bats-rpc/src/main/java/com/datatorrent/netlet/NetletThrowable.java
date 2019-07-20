/*
 * Copyright (c) 2013 DataTorrent, Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.netlet;

/**
 * <p>NetletThrowable interface.</p>
 *
 * @since 1.0.0
 */
public interface NetletThrowable
{
  EventLoop getEventLoop();

  Throwable getCause();

  class Util
  {
    public static NetletThrowable rewrap(Throwable th, EventLoop el)
    {
      if (th instanceof NetletThrowable) {
        NetletThrowable nth = (NetletThrowable)th;
        if (el == nth.getEventLoop()) {
          return nth;
        }

        return rewrap(nth.getCause(), el);
      }

      if (th instanceof Error) {
        return new NetletError((Error)th, el);
      }

      if (th instanceof RuntimeException) {
        return new NetletRuntimeException((RuntimeException)th, el);
      }

      return new NetletException((Exception)th, el);
    }

    public static void throwRuntime(NetletThrowable cause)
    {
      if (cause instanceof NetletError) {
        throw (NetletError)cause;
      }

      if (cause instanceof RuntimeException) {
        throw (NetletRuntimeException)cause;
      }

      throw new NetletRuntimeException(new RuntimeException(cause.getCause()), cause.getEventLoop());
    }

    public static void rethrow(NetletThrowable cause) throws NetletException
    {
      if (cause instanceof NetletError) {
        throw (NetletError)cause;
      }

      if (cause instanceof RuntimeException) {
        throw (NetletRuntimeException)cause;
      }

      throw (NetletException)cause;
    }

  }

  class NetletError extends Error implements NetletThrowable
  {
    public final transient EventLoop eventloop;

    public NetletError(Error error, EventLoop el)
    {
      super(error);
      eventloop = el;
    }

    @Override
    public EventLoop getEventLoop()
    {
      return eventloop;
    }

    private static final long serialVersionUID = 201401221632L;
  }

  class NetletException extends Exception implements NetletThrowable
  {
    public final transient EventLoop eventloop;

    public NetletException(Exception exception, EventLoop el)
    {
      super(exception);
      eventloop = el;
    }

    @Override
    public EventLoop getEventLoop()
    {
      return eventloop;
    }

    private static final long serialVersionUID = 201401221635L;
  }

  class NetletRuntimeException extends RuntimeException implements NetletThrowable
  {
    public final transient EventLoop eventloop;

    public NetletRuntimeException(RuntimeException exception, EventLoop el)
    {
      super(exception);
      eventloop = el;
    }

    @Override
    public EventLoop getEventLoop()
    {
      return eventloop;
    }

    private static final long serialVersionUID = 201401221638L;
  }

}
