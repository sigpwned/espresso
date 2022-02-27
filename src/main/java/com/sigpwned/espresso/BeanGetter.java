package com.sigpwned.espresso;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
