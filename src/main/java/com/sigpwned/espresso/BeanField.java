package com.sigpwned.espresso;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;
import com.sigpwned.espresso.util.Beans;

public class BeanField implements BeanElement {
  private final Field field;

  public BeanField(Field field) {
    if (!Beans.isBeanField(field))
      throw new IllegalArgumentException("not a bean field");
    this.field = field;
  }

  @Override
  public String getName() {
    return getField().getName();
  }

  @Override
  public Type getGenericType() {
    return getField().getGenericType();
  }

  public boolean isGettable() {
    return Modifier.isPublic(getField().getModifiers());
  }

  public boolean isSettable() {
    return Modifier.isPublic(getField().getModifiers());
  }

  public Object get(Object instance) throws IllegalArgumentException, IllegalAccessException {
    if (!isGettable())
      throw new UnsupportedOperationException();
    return getField().get(instance);
  }

  public void set(Object instance, Object value)
      throws IllegalArgumentException, IllegalAccessException {
    if (!isSettable())
      throw new UnsupportedOperationException();
    getField().set(instance, value);
  }

  public Field getField() {
    return field;
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(field);
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
    BeanField other = (BeanField) obj;
    return Objects.equals(field, other.field);
  }

  @Override
  @Generated
  public String toString() {
    return "BeanField [field=" + field + "]";
  }
}
