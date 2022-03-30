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

import static java.util.Arrays.asList;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;
import com.sigpwned.espresso.util.Beans;

/**
 * A wrapper for a getter method that is part of the physical implementation of a logical property.
 */
public class BeanGetter implements BeanElement {
  private final Method method;

  public BeanGetter(Method method) {
    if (!Beans.isBeanGetter(method))
      throw new IllegalArgumentException("not a getter method");
    this.method = method;
  }

  @Override
  public String getName() {
    String methodName = getMethod().getName();
    if (methodName.startsWith("get")) {
      return Character.toLowerCase(methodName.charAt(3))
          + methodName.substring(4, methodName.length());
    } else if (methodName.startsWith("is")) {
      return Character.toLowerCase(methodName.charAt(2))
          + methodName.substring(3, methodName.length());
    } else {
      // I have no idea what this method name is.
      throw new AssertionError("unrecognized getter name " + getName());
    }
  }

  @Override
  public Type getGenericType() {
    return getMethod().getGenericReturnType();
  }
  
  @Override
  public List<Annotation> getAnnotations() {
    return asList(getMethod().getAnnotations());
  }

  @Override
  public boolean isGettable() {
    return true;
  }

  @Override
  public Object get(Object instance) throws InvocationTargetException {
    try {
      return getMethod().invoke(instance);
    } catch (IllegalAccessException e) {
      // We've ensured that the method is public. This should never happen.
      throw new AssertionError("getter is not accessible", e);
    }
  }

  public Method getMethod() {
    return method;
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(method);
  }

  @Override
  @Generated
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BeanGetter other = (BeanGetter) obj;
    return Objects.equals(method, other.method);
  }

  @Override
  @Generated
  public String toString() {
    return "BeanGetter [method=" + method + "]";
  }
}
