package com.sigpwned.espresso;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * An abstract representation of a syntactical element that references a logical property, e.g. a
 * field, a getter method, or a setter method.
 */
public interface BeanElement {
  public String getName();

  public Type getGenericType();

  /**
   * Returns true if this element can read the logical property
   */
  default boolean isGettable() {
    return false;
  }

  /**
   * Returns true if this element can write the logical property
   */
  default boolean isSettable() {
    return false;
  }

  /**
   * Attempts to read the value of the logical property from the given instance.
   * 
   * @throws IllegalArgumentException if the given instance is not valid
   * @throws InvocationTargetException if this element is a method and invoking the method generates
   *         an exception
   */
  default Object get(Object instance) throws InvocationTargetException {
    throw new UnsupportedOperationException();
  }

  /**
   * Attempts to write the value of the logical property to the given instance.
   * 
   * @throws IllegalArgumentException if the given instance or value is not valid
   * @throws InvocationTargetException if this element is a method and invoking the method generates
   *         an exception
   */
  default void set(Object instance, Object value) throws InvocationTargetException {
    throw new UnsupportedOperationException();
  }
}
