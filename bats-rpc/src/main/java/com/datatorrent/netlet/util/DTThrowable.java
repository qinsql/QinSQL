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

/**
 * Helper method to ensure that exceptions are propertly thrown.
 * If the cause is of type Error or RuntimeException then the
 * cause it thrown as it is. Otherwise the cause is wrapped in
 * a RuntimeException and the later is thrown.
 *
 * @since 1.0.0
 */
public class DTThrowable
{
  @SuppressWarnings("ThrowableResultIgnored")
  public static void rethrow(Throwable cause)
  {
    wrapIfChecked(cause);
  }

  @SuppressWarnings("ThrowableResultIgnored")
  public static void rethrow(Exception exception)
  {
    wrapIfChecked(exception);
  }

  public static RuntimeException wrapIfChecked(Throwable cause)
  {
    if (cause instanceof Error) {
      throw (Error)cause;
    }

    if (cause instanceof RuntimeException) {
      throw (RuntimeException)cause;
    }

    throw new RuntimeException(cause);
  }

  public static RuntimeException wrapIfChecked(Exception exception)
  {
    if (exception instanceof RuntimeException) {
      throw (RuntimeException)exception;
    }

    throw new RuntimeException(exception);
  }

  /**
   *
   * @param error
   * @return
   * @deprecated Error does not need to be wrapped; Instead of "DTThrowable.wrapIfChecked(error);" use "throw error;" directly.
   */
  @Deprecated
  public static RuntimeException wrapIfChecked(Error error)
  {
    throw error;
  }

  /**
   *
   * @param exception
   * @return
   * @deprecated Unchecked exception (subclass of RuntimeException) does not need to be wrapped; Instead of "DTThrowable.rethrow(runtime_exception);" use "throw runtime_exception;" directly.
   */
  @Deprecated
  public static RuntimeException wrapIfChecked(RuntimeException exception)
  {
    throw exception;
  }

  /**
   *
   * @param error
   * @deprecated Instead "DTThrowable.rethrow(error);" use "throw error;" directly.
   */
  @Deprecated
  public static void rethrow(Error error)
  {
    throw error;
  }

  /**
   *
   * @param exception
   * @deprecated Instead "DTThrowable.rethrow(runtime_exception);" use "throw runtime_exception;" directly.
   */
  @Deprecated
  public static void rethrow(RuntimeException exception)
  {
    throw exception;
  }

}
