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

import static java.lang.String.format;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;

/**
 * An instance of a {@link BeanClass}.
 */
public class BeanInstance {
  public static BeanInstance wrap(Object instance) {
    if (instance == null)
      throw new NullPointerException();
    return new BeanInstance(BeanClass.scan(instance.getClass()), instance);
  }

  private final BeanClass beanClass;
  private final Object instance;

  protected BeanInstance(BeanClass beanClass, Object instance) {
    this.beanClass = beanClass;
    this.instance = instance;
  }

  /**
   * The {@link BeanClass} for this instance.
   */
  public BeanClass getBeanClass() {
    return beanClass;
  }

  /**
   * The wrapped instance object. This wrapper is a view on this object, so any changes to this
   * underlying delegate instance will be reflected in this object, and vice versa.
   */
  public Object getInstance() {
    return instance;
  }

  /**
   * Gets the value of the named property in this instance.
   * 
   * @throws InvocationTargetException if the underlying getter throws an exception
   */
  public Object get(String name) throws InvocationTargetException {
    return get(getBeanClass().getProperty(name)
        .orElseThrow(() -> new IllegalArgumentException(format("No such property %s", name))));
  }

  /**
   * Gets the value of the given property in this instance.
   * 
   * @throws InvocationTargetException if the underlying getter throws an exception
   */
  public Object get(BeanProperty property) throws InvocationTargetException {
    if (!property.getBeanClass().equals(getBeanClass()))
      throw new IllegalArgumentException(format("Given property belongs to %s, not %s",
          property.getBeanClass().getRawType(), getBeanClass().getRawType()));
    return property.get(getInstance());
  }

  /**
   * Gets the value of the named property in this instance.
   * 
   * @throws InvocationTargetException if the underlying setter throws an exception
   */
  public void set(String name, Object value) throws InvocationTargetException {
    set(getBeanClass().getProperty(name).orElseThrow(
        () -> new IllegalArgumentException(format("No such property %s", name))), value);
  }

  /**
   * Sets the value of the given property in this instance.
   * 
   * @throws InvocationTargetException if the underlying setter throws an exception
   */
  public void set(BeanProperty property, Object value) throws InvocationTargetException {
    if (!property.getBeanClass().equals(getBeanClass()))
      throw new IllegalArgumentException(format("Given property belongs to %s, not %s",
          property.getBeanClass().getRawType(), getBeanClass().getRawType()));
    property.set(getInstance(), value);
  }

  @Override
  @Generated
  public int hashCode() {
    return getInstance().hashCode();
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
    BeanInstance other = (BeanInstance) obj;
    return Objects.equals(beanClass, other.beanClass) && Objects.equals(instance, other.instance);
  }

  @Override
  public String toString() {
    return getInstance().toString();
  }
}
