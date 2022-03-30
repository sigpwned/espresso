/*-
 * =================================LICENSE_START==================================
 * espresso
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.espresso.util;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for processing classes, fields, and methods using reflection.
 */
public final class Reflection {
  private Reflection() {}

  /**
   * Returns all declared fields from the given type as a list.
   *
   * @see Class#getDeclaredFields()
   */
  public static List<Field> getDeclaredFields(Class<?> rawType) {
    return asList(rawType.getDeclaredFields());
  }

  /**
   * Returns all declared fields from the given type and all its ancestors. Fields from the given
   * type appear first in the result, followed by fields from the given type's parent, and so on.
   *
   * @see Class#getDeclaredFields()
   */
  public static List<Field> getAllDeclaredFields(Class<?> rawType) {
    List<Field> result = new ArrayList<>();
    for (Class<?> c = rawType; c != null; c = c.getSuperclass())
      result.addAll(getDeclaredFields(c));
    return unmodifiableList(result);
  }

  /**
   * Returns all declared methods from the given type as a list.
   *
   * @see Class#getDeclaredMethods()
   */
  public static List<Method> getDeclaredMethods(Class<?> rawType) {
    return asList(rawType.getDeclaredMethods());
  }

  /**
   * Returns all declared methods from the given type and all its ancestors. Methods from the given
   * type appear first in the result, followed by methods from the given type's parent, and so on.
   *
   * @see Class#getDeclaredMethods()
   */
  public static List<Method> getAllDeclaredMethods(Class<?> rawType) {
    List<Method> result = new ArrayList<>();
    for (Class<?> c = rawType; c != null; c = c.getSuperclass())
      result.addAll(getDeclaredMethods(c));
    return unmodifiableList(result);
  }
}
