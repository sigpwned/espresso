package com.sigpwned.espresso;

import static java.lang.String.format;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import com.sigpwned.espresso.annotation.Generated;

public class BeanInstance {
  public static BeanInstance wrap(Object instance) {
    if (instance == null)
      throw new NullPointerException();
    return new BeanInstance(BeanClass.scan(instance.getClass()), instance);
  }

  private final BeanClass beanClass;
  private final Object instance;

  protected BeanInstance(BeanClass beanClass, Object instance) {
    this.beanClass = beanClass;
    this.instance = instance;
  }

  /**
   * @return the beanClass
   */
  public BeanClass getBeanClass() {
    return beanClass;
  }

  /**
   * @return the instance
   */
  public Object getInstance() {
    return instance;
  }

  public Object get(String name) throws InvocationTargetException {
    return get(getBeanClass().getProperty(name)
        .orElseThrow(() -> new IllegalArgumentException(format("No such property %s", name))));
  }

  public Object get(BeanProperty property) throws InvocationTargetException {
    if (!property.getBeanClass().equals(getBeanClass()))
      throw new IllegalArgumentException(format("Given property belongs to %s, not %s",
          property.getBeanClass().getRawType(), getBeanClass().getRawType()));
    return property.get(getInstance());
  }

  public void set(String name, Object value) throws InvocationTargetException {
    set(getBeanClass().getProperty(name).orElseThrow(
        () -> new IllegalArgumentException(format("No such property %s", name))), value);
  }

  public void set(BeanProperty property, Object value) throws InvocationTargetException {
    if (!property.getBeanClass().equals(getBeanClass()))
      throw new IllegalArgumentException(format("Given property belongs to %s, not %s",
          property.getBeanClass().getRawType(), getBeanClass().getRawType()));
    property.set(getInstance(), value);
  }

  @Override
  @Generated
  public int hashCode() {
    return getInstance().hashCode();
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
    BeanInstance other = (BeanInstance) obj;
    return Objects.equals(beanClass, other.beanClass) && Objects.equals(instance, other.instance);
  }

  @Override
  public String toString() {
    return getInstance().toString();
  }
}
