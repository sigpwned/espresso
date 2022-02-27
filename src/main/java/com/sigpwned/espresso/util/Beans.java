package com.sigpwned.espresso.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Beans {
  private Beans() {}

  /**
   * <p>
   * A {@link Method} is a "bean getter" if:
   * </p>
   * 
   * <ul>
   * <li>It is public</li>
   * <li>It is not synthetic</li>
   * <li>It is not static</li>
   * <li>It is not native</li>
   * <li>Its name starts with "get" followed by an uppercase letter</li>
   * <li>Its return type is not void</li>
   * <li>It takes no parameters</li>
   * </ul>
   */
  public static boolean isBeanGetter(Method method) {
    return !method.isSynthetic() && method.getName().length() > 3
        && method.getName().startsWith("get")
        && Character.isUpperCase(method.getName().codePointAt(3))
        && !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
        && !Modifier.isNative(method.getModifiers())
        && !method.getGenericReturnType().equals(void.class) && method.getParameterCount() == 0;
  }

  /**
   * <p>
   * A {@link Method} is a "bean setter" if:
   * </p>
   * 
   * <ul>
   * <li>It is public</li>
   * <li>It is not synthetic</li>
   * <li>It is not static</li>
   * <li>It is not native</li>
   * <li>Its name starts with "set" followed by an uppercase letter</li>
   * <li>Its return type is void</li>
   * <li>It takes one parameter</li>
   * </ul>
   */
  public static boolean isBeanSetter(Method method) {
    return !method.isSynthetic() && method.getName().length() > 3
        && method.getName().startsWith("set")
        && Character.isUpperCase(method.getName().codePointAt(3))
        && !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
        && !Modifier.isNative(method.getModifiers())
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

  public static String upperCamelToLowerCamel(String s) {
    return Character.toLowerCase(s.charAt(0)) + s.substring(1, s.length());
  }

  public static String lowerCamelToUpperCamel(String s) {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1, s.length());
  }
}
