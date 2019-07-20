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
package com.datatorrent.netlet.util;

import java.util.concurrent.BlockingQueue;

/**
 * <p>UnsafeBlockingQueue interface.</p>
 *
 * @param <T> type of the objects in this queue.
 * @since 1.0.0
 */
public interface UnsafeBlockingQueue<T> extends BlockingQueue<T>
{
  /**
   * Retrieves and removes the head of this queue.
   *
   * This method should be called only when the callee knows that the head is present. It skips
   * the checks that poll does hence you may get unreliable results if you use it without checking
   * for the presence of the head first.
   *
   * @return the head of this queue.
   */
  public T pollUnsafe();

  public T peekUnsafe();
}
