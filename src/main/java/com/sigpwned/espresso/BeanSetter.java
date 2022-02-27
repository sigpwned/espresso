package com.sigpwned.espresso;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;
import com.sigpwned.espresso.util.Beans;

public class BeanSetter implements BeanElement {
  private final Method method;

  public BeanSetter(Method method) {
    if (!Beans.isBeanSetter(method))
      throw new IllegalArgumentException("not a setter method");
    this.method = method;
  }

  @Override
  public String getName() {
    String methodName = getMethod().getName();
    return Character.toLowerCase(methodName.charAt(3))
        + methodName.substring(4, methodName.length());
  }

  @Override
  public Type getGenericType() {
    return getMethod().getGenericParameterTypes()[0];
  }

  public void apply(Object instance, Object value) throws InvocationTargetException {
    try {
      getMethod().invoke(instance, value);
    } catch (IllegalAccessException e) {
      // We checked to make sure this method is public. This should never happen.
      throw new AssertionError("setter is not accessible", e);
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
    BeanSetter other = (BeanSetter) obj;
    return Objects.equals(method, other.method);
  }

  @Override
  @Generated
  public String toString() {
    return "BeanSetter [method=" + method + "]";
  }
}