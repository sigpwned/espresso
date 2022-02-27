package com.sigpwned.espresso;

import static java.util.stream.Collectors.toList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;

public class BeanProperty {
  private final BeanClass beanClass;
  private final BeanField field;
  private final BeanGetter getter;
  private final BeanSetter setter;

  public BeanProperty(BeanClass beanClass, BeanField field, BeanGetter getter, BeanSetter setter) {
    this.beanClass = beanClass;

    List<BeanElement> elements = new ArrayList<>(3);
    if (field != null)
      elements.add(field);
    if (getter != null)
      elements.add(getter);
    if (setter != null)
      elements.add(setter);

    // We have to have at least one element
    if (elements.isEmpty())
      throw new IllegalArgumentException("no field, getter, or setter");

    // We should be gettable
    if (getter == null && (field == null || !field.isGettable()))
      throw new IllegalArgumentException("not gettable");

    // We should be settable
    if (setter == null && (field == null || !field.isSettable()))
      throw new IllegalArgumentException("not settable");

    // All the element names have to match exactly
    List<String> names = elements.stream().map(BeanElement::getName).distinct().collect(toList());
    if (names.size() > 1)
      throw new IllegalArgumentException("names mismatch: " + names);

    // All of the types have to match exactly
    List<Type> genericTypes =
        elements.stream().map(BeanElement::getGenericType).distinct().collect(toList());
    if (genericTypes.size() > 1)
      throw new IllegalArgumentException("types mismatch: " + genericTypes);

    this.field = field;
    this.getter = getter;
    this.setter = setter;
  }

  public String getName() {
    return getAnyElement().getName();
  }

  public Type getGenericType() {
    return getAnyElement().getGenericType();
  }

  /**
   * Attempts to retrieve the value of this property from the given instance. It will prefer to use
   * a getter, if present.
   */
  public Object get(Object instance) throws InvocationTargetException {
    if (getGetter() != null)
      return getGetter().apply(instance);
    if (getField() != null && getField().isGettable()) {
      try {
        return getField().get(instance);
      } catch (IllegalAccessException e) {
        // We confirmed that the field is public. This should never happen.
        throw new AssertionError("field is not accessible", e);
      }
    }
    // This should never happen due to if guard
    throw new AssertionError("not gettable");
  }

  /**
   * Attempts to assign the value of this property to the given instance. It will prefer to use a
   * setter, if present.
   */
  public void set(Object instance, Object value) throws InvocationTargetException {
    if (getSetter() != null) {
      getSetter().apply(instance, value);
      return;
    }
    if (getField() != null && getField().isSettable()) {
      try {
        getField().set(instance, value);
        return;
      } catch (IllegalAccessException e) {
        // We confirmed that the field is public. This should never happen.
        throw new AssertionError("field is not accessible", e);
      }
    }
    // This should never happen due to if guard
    throw new AssertionError("not settable");
  }

  private BeanElement getAnyElement() {
    if (getField() != null)
      return getField();
    if (getGetter() != null)
      return getGetter();
    if (getSetter() != null)
      return getSetter();
    // This should never happen due to checks in constructor
    throw new AssertionError("no field, getter, or setter");
  }

  /**
   * @return the beanClass
   */
  public BeanClass getBeanClass() {
    return beanClass;
  }

  /**
   * @return the field
   */
  private BeanField getField() {
    return field;
  }

  /**
   * @return the getter
   */
  private BeanGetter getGetter() {
    return getter;
  }

  /**
   * @return the setter
   */
  private BeanSetter getSetter() {
    return setter;
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(field, getter, setter);
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
    return Objects.equals(field, other.field) && Objects.equals(getter, other.getter)
        && Objects.equals(setter, other.setter);
  }

  @Override
  @Generated
  public String toString() {
    return "BeanProperty [field=" + field + ", getter=" + getter + ", setter=" + setter + "]";
  }
}
