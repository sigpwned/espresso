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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Utility methods for scanning beans
 */
public final class Beans {
  private Beans() {}

  /**
   * <p>
   * A {@link Method} is a "bean getter" if:
   * </p>
   * 
   * <ul>
   * <li>It is public</li>
   * <li>It is not static</li>
   * <li>Its name starts with "get" followed by an uppercase letter</li>
   * <li>If it's a boolean property, the name may also start with "is" followed by an uppercase letter</li>
   * <li>Its return type is not void</li>
   * <li>It takes no parameters</li>
   * </ul>
   */
  public static boolean isBeanGetter(Method method) {
    if (!Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
        && !method.getGenericReturnType().equals(void.class) && method.getParameterCount() == 0) {
      if (method.getName().length() > 3 && method.getName().startsWith("get")
          && Character.isUpperCase(method.getName().codePointAt(3))) {
        return true;
      } else if (method.getGenericReturnType().equals(boolean.class)
          && method.getName().length() > 2 && method.getName().startsWith("is")
          && Character.isUpperCase(method.getName().codePointAt(2))) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * <p>
   * A {@link Method} is a "bean setter" if:
   * </p>
   * 
   * <ul>
   * <li>It is public</li>
   * <li>It is not static</li>
   * <li>Its name starts with "set" followed by an uppercase letter</li>
   * <li>Its return type is void</li>
   * <li>It takes one parameter</li>
   * </ul>
   */
  public static boolean isBeanSetter(Method method) {
    return method.getName().length() > 3 && method.getName().startsWith("set")
        && Character.isUpperCase(method.getName().codePointAt(3))
        && !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
        && method.getGenericReturnType().equals(void.class) && method.getParameterCount() == 1;
  }

  /**
   * <p>
   * A {@link Field} is a "bean field" if:
   * </p>
   * 
   * <ul>
   * <li>Its name starts with a lowercase letter</li>
   * <li>It is not static</li>
   * <li>It is not final</li>
   * </ul>
   */
  public static boolean isBeanField(Field field) {
    return Character.isLowerCase(field.getName().charAt(0))
        && !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
  }
}
