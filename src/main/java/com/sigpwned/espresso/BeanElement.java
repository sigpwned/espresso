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
package com.sigpwned.espresso;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * An abstract representation of a syntactical element that references a logical property, e.g. a
 * field, a getter method, or a setter method.
 */
public interface BeanElement {
  public String getName();

  public Type getGenericType();
  
  public List<Annotation> getAnnotations();

  /**
   * Returns true if this element can read the logical property
   */
  default boolean isGettable() {
    return false;
  }

  /**
   * Returns true if this element can write the logical property
   */
  default boolean isSettable() {
    return false;
  }

  /**
   * Attempts to read the value of the logical property from the given instance.
   * 
   * @throws IllegalArgumentException if the given instance is not valid
   * @throws InvocationTargetException if this element is a method and invoking the method generates
   *         an exception
   */
  default Object get(Object instance) throws InvocationTargetException {
    throw new UnsupportedOperationException();
  }

  /**
   * Attempts to write the value of the logical property to the given instance.
   * 
   * @throws IllegalArgumentException if the given instance or value is not valid
   * @throws InvocationTargetException if this element is a method and invoking the method generates
   *         an exception
   */
  default void set(Object instance, Object value) throws InvocationTargetException {
    throw new UnsupportedOperationException();
  }
}
