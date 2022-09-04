/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.util;

import java.lang.reflect.Field;

public class PluginUtils {

    /** Creates an instance of a plugin class. First looks for a static
     * member called INSTANCE, then calls a public default constructor.
     *
     * <p>If className contains a "#" instead looks for a static field.
     *
     * @param pluginClass Class (or interface) to instantiate
     * @param className Name of implementing class
     * @param <T> Class
     * @return Plugin instance
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T instantiatePlugin(Class<T> pluginClass, String className) {
        try {
            // Given a static field, say "com.example.MyClass#FOO_INSTANCE", return
            // the value of that static field.
            if (className.contains("#")) {
                try {
                    int i = className.indexOf('#');
                    String left = className.substring(0, i);
                    String right = className.substring(i + 1);
                    // noinspection unchecked
                    final Class<T> clazz = (Class) Class.forName(left);
                    final Field field;
                    field = clazz.getField(right);
                    return pluginClass.cast(field.get(null));
                } catch (NoSuchFieldException e) {
                    // ignore
                }
            }
            // noinspection unchecked
            final Class<T> clazz = (Class) Class.forName(className);
            assert pluginClass.isAssignableFrom(clazz);
            try {
                // We assume that if there is an INSTANCE field it is static and
                // has the right type.
                final Field field = clazz.getField("INSTANCE");
                return pluginClass.cast(field.get(null));
            } catch (NoSuchFieldException e) {
                // ignore
            }
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Property '" + className + "' not valid for plugin type " + pluginClass.getName(), e);
        }
    }
}
