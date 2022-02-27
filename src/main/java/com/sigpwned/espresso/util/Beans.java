package com.sigpwned.espresso.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Utility methods for scanning beans
 */
public final class Beans {
  private Beans() {}

  /**
   * <p>
   * A {@link Method} is a "bean getter" if:
   * </p>
   * 
   * <ul>
   * <li>It is public</li>
   * <li>It is not static</li>
   * <li>Its name starts with "get" followed by an uppercase letter</li>
   * <li>If it's a boolean property, the name may also start with "is" followed by an uppercase letter</li>
   * <li>Its return type is not void</li>
   * <li>It takes no parameters</li>
   * </ul>
   */
  public static boolean isBeanGetter(Method method) {
    if (!Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
        && !method.getGenericReturnType().equals(void.class) && method.getParameterCount() == 0) {
      if (method.getName().length() > 3 && method.getName().startsWith("get")
          && Character.isUpperCase(method.getName().codePointAt(3))) {
        return true;
      } else if (method.getGenericReturnType().equals(boolean.class)
          && method.getName().length() > 2 && method.getName().startsWith("is")
          && Character.isUpperCase(method.getName().codePointAt(2))) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * <p>
   * A {@link Method} is a "bean setter" if:
   * </p>
   * 
   * <ul>
   * <li>It is public</li>
   * <li>It is not static</li>
   * <li>Its name starts with "set" followed by an uppercase letter</li>
   * <li>Its return type is void</li>
   * <li>It takes one parameter</li>
   * </ul>
   */
  public static boolean isBeanSetter(Method method) {
    return method.getName().length() > 3 && method.getName().startsWith("set")
        && Character.isUpperCase(method.getName().codePointAt(3))
        && !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
        && method.getGenericReturnType().equals(void.class) && method.getParameterCount() == 1;
  }

  /**
   * <p>
   * A {@link Field} is a "bean field" if:
   * </p>
   * 
   * <ul>
   * <li>Its name starts with a lowercase letter</li>
   * <li>It is not static</li>
   * <li>It is not final</li>
   * </ul>
   */
  public static boolean isBeanField(Field field) {
    return Character.isLowerCase(field.getName().charAt(0))
        && !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
  }
}
