package com.sigpwned.espresso;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
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
    if (!getElements().stream().anyMatch(BeanElement::isGettable))
      throw new IllegalArgumentException("not gettable");

    // We should be settable
    if (!getElements().stream().anyMatch(BeanElement::isSettable))
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
   * Attempts to retrieve the value of this property from the given instance. It will prefer to use
   * a getter, if present.
   * 
   * @throw InvocationTargetException if a getter is invoked and it generates an exception
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
   * @throw InvocationTargetException if a setter is invoked and it generates an exception
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
