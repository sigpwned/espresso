package com.sigpwned.espresso.util;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class Reflection {
  private Reflection() {}

  /**
   * Returns all declared fields from the given type as a list.
   *
   * @see Class#getDeclaredFields()
   */
  public static List<Field> getDeclaredFields(Class<?> rawType) {
    return asList(rawType.getDeclaredFields());
  }

  /**
   * Returns all declared fields from the given type and all its ancestors. Fields from the given
   * type appear first in the result, followed by fields from the given type's parent, and so on.
   *
   * @see Class#getDeclaredFields()
   */
  public static List<Field> getAllDeclaredFields(Class<?> rawType) {
    List<Field> result = new ArrayList<>();
    for (Class<?> c = rawType; c != null; c = c.getSuperclass())
      result.addAll(getDeclaredFields(c));
    return unmodifiableList(result);
  }

  /**
   * Returns all declared methods from the given type as a list.
   *
   * @see Class#getDeclaredMethods()
   */
  public static List<Method> getDeclaredMethods(Class<?> rawType) {
    return asList(rawType.getDeclaredMethods());
  }

  /**
   * Returns all declared methods from the given type and all its ancestors. Methods from the given
   * type appear first in the result, followed by methods from the given type's parent, and so on.
   *
   * @see Class#getDeclaredMethods()
   */
  public static List<Method> getAllDeclaredMethods(Class<?> rawType) {
    List<Method> result = new ArrayList<>();
    for (Class<?> c = rawType; c != null; c = c.getSuperclass())
      result.addAll(getDeclaredMethods(c));
    return unmodifiableList(result);
  }
}
