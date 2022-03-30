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

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;

/**
 * A model object and accessor for a logical property
 */
public class BeanProperty {
  private final BeanClass beanClass;
  private final List<BeanElement> elements;

  /* default */ BeanProperty(BeanClass beanClass, BeanField field, BeanGetter getter,
      BeanSetter setter) {
    this.beanClass = beanClass;

    // We should prefer a getter or setter when present, so make sure those are first in the list.
    List<BeanElement> elements = new ArrayList<>(3);
    if (getter != null)
      elements.add(getter);
    if (setter != null)
      elements.add(setter);
    if (field != null)
      elements.add(field);

    // We have to have at least one element
    if (elements.isEmpty())
      throw new IllegalArgumentException("no field, getter, or setter");

    // Store our elements
    this.elements = unmodifiableList(elements);

    // We should be gettable
    if (getElements().stream().noneMatch(BeanElement::isGettable))
      throw new IllegalArgumentException("not gettable");

    // We should be settable
    if (getElements().stream().noneMatch(BeanElement::isSettable))
      throw new IllegalArgumentException("not settable");

    // All the element names have to match exactly
    List<String> names =
        getElements().stream().map(BeanElement::getName).distinct().collect(toList());
    if (names.size() > 1)
      throw new IllegalArgumentException("names mismatch: " + names);

    // All of the types have to match exactly
    List<Type> genericTypes =
        getElements().stream().map(BeanElement::getGenericType).distinct().collect(toList());
    if (genericTypes.size() > 1)
      throw new IllegalArgumentException("types mismatch: " + genericTypes);
  }

  /**
   * The name of this property. If the property has a field, then it will match the name of the
   * field.
   */
  public String getName() {
    return getAnyElement().getName();
  }

  /**
   * The type of this property. Note that this type must match across all field, getter, and setter
   * elements that are defined for this property.
   */
  public Type getGenericType() {
    return getAnyElement().getGenericType();
  }

  /**
   * Returns all of the annotations present on any fields, getters, and setters that comprise this
   * property. Annotations from each individual element will appear in the same order as their
   * declarations, but there is no guarantee about the order in which each elements' annotations
   * will appear relative to the other elements.
   */
  public List<Annotation> getAnnotations() {
    return getElements().stream().flatMap(e -> e.getAnnotations().stream()).collect(toList());
  }

  /**
   * Attempts to retrieve the value of this property from the given instance. It will prefer to use
   * a getter, if present.
   * 
   * @throws InvocationTargetException if a getter is invoked and it generates an exception
   * @throws IllegalArgumentException if the given instance is not of the correct type
   */
  public Object get(Object instance) throws InvocationTargetException {
    return getElements().stream().filter(BeanElement::isGettable).findFirst()
        .orElseThrow(() -> new AssertionError("not gettable")).get(instance);
  }

  /**
   * Attempts to assign the value of this property to the given instance. It will prefer to use a
   * setter, if present.
   * 
   * @throws InvocationTargetException if a setter is invoked and it generates an exception
   * @throws IllegalArgumentException if either the given instance or the given value is not of the
   *         correct type
   */
  public void set(Object instance, Object value) throws InvocationTargetException {
    getElements().stream().filter(BeanElement::isSettable).findFirst()
        .orElseThrow(() -> new AssertionError("not settable")).set(instance, value);
  }

  private BeanElement getAnyElement() {
    return getElements().get(0);
  }

  /**
   * @return the beanClass
   */
  public BeanClass getBeanClass() {
    return beanClass;
  }

  /**
   * @return the elements
   */
  private List<BeanElement> getElements() {
    return elements;
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(beanClass, elements);
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
    BeanProperty other = (BeanProperty) obj;
    return Objects.equals(beanClass, other.beanClass) && Objects.equals(elements, other.elements);
  }

  @Override
  @Generated
  public String toString() {
    return "BeanProperty [beanClass=" + beanClass + ", elements=" + elements + "]";
  }
}
