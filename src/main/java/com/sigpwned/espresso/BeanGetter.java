package com.sigpwned.espresso;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;
import com.sigpwned.espresso.util.Beans;

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
    return Character.toLowerCase(methodName.charAt(3))
        + methodName.substring(4, methodName.length());
  }

  @Override
  public Type getGenericType() {
    return getMethod().getGenericReturnType();
  }

  public Object apply(Object instance)
      throws InvocationTargetException {
        try {
      return getMethod().invoke(instance);
    }
    catch(IllegalAccessException e) {
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
