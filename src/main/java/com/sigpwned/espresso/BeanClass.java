package com.sigpwned.espresso;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.synchronizedMap;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sigpwned.espresso.annotation.Generated;
import com.sigpwned.espresso.util.Beans;
import com.sigpwned.espresso.util.Reflection;

public class BeanClass implements Iterable<BeanProperty> {
  private static final Logger LOGGER = LoggerFactory.getLogger(BeanClass.class);

  private static final int BEAN_CLASS_CACHE_SIZE =
      Optional.ofNullable(System.getenv("ESPRESSO_BEAN_CLASS_CACHE_SIZE")).map(Integer::parseInt)
          .orElse(100);

  /* default */ static final Map<Class<?>, BeanClass> CACHE = synchronizedMap(new LinkedHashMap<>() {
    private static final long serialVersionUID = 5830101232146989304L;

    @Override
    protected boolean removeEldestEntry(Map.Entry<Class<?>, BeanClass> eldest) {
      return size() >= BEAN_CLASS_CACHE_SIZE;
    }
  });

  /**
   * Must be a visible, concrete, non-void, non-primitive, non-array class with a default
   * constructor
   */
  public static BeanClass scan(Class<?> rawType) {
    BeanClass cached = CACHE.get(rawType);
    if (cached != null)
      return cached;

    // This is the void tyep
    if (rawType.equals(void.class))
      throw new IllegalArgumentException(format("Class %s is void", rawType.getName()));

    // This is an array class
    if (rawType.getComponentType() != null)
      throw new IllegalArgumentException(format("Class %s is array type", rawType.getName()));

    // This is a primitive type
    if (rawType.isPrimitive())
      throw new IllegalArgumentException(format("Class %s is primitive", rawType.getName()));

    // This is an abstract class
    if (Modifier.isAbstract(rawType.getModifiers()))
      throw new IllegalArgumentException(format("Class %s is abstract", rawType.getName()));

    // We need a default constructor
    Constructor<?> defaultConstructor;
    try {
      defaultConstructor = rawType.getConstructor();
    } catch (NoSuchMethodException e) {
      // The bean standard requires a default constructor
      throw new IllegalArgumentException(
          format("Class %s has no default constructor", rawType.getName()));
    }

    // The default constructor is not public
    if (!Modifier.isPublic(defaultConstructor.getModifiers()))
      throw new IllegalArgumentException(
          format("Class %s default constructor is not public", rawType.getName()));

    // We should be able to instantiate the class
    try {
      defaultConstructor.newInstance();
    } catch (InstantiationException e) {
      // We can't instantiate this class for some reason.
      throw new IllegalArgumentException(
          format("Class %s could not be instantiated", rawType.getName()), e);
    } catch (IllegalAccessException e) {
      // We can't see this class for some reason.
      throw new IllegalArgumentException(
          format("Class %s could not be accessed", rawType.getName()), e);
    } catch (IllegalArgumentException e) {
      // We don't pass arguments, so this should never happen.
      throw new AssertionError("failed to create class instance", e);
    } catch (InvocationTargetException e) {
      // This class fails to instantiate
      throw new IllegalArgumentException(
          format("Class %s failed during instantiation", rawType.getName()), e);
    }

    BeanClass result = new BeanClass(rawType, defaultConstructor);

    Map<String, List<BeanField>> fields =
        Reflection.getAllDeclaredFields(rawType).stream().filter(Beans::isBeanField)
            .map(BeanField::new).collect(groupingBy(BeanField::getName, toList()));

    List<Method> methods = Reflection.getAllDeclaredMethods(rawType);

    Map<String, List<BeanGetter>> getters = methods.stream().filter(Beans::isBeanGetter)
        .map(BeanGetter::new).collect(groupingBy(BeanGetter::getName, toList()));

    Map<String, List<BeanSetter>> setters = methods.stream().filter(Beans::isBeanSetter)
        .map(BeanSetter::new).collect(groupingBy(BeanSetter::getName, toList()));

    Set<String> propertyNames = new TreeSet<>();
    propertyNames.addAll(fields.keySet());
    propertyNames.addAll(getters.keySet());
    propertyNames.addAll(setters.keySet());

    for (String propertyName : propertyNames) {
      List<BeanField> propertyFields = fields.getOrDefault(propertyName, emptyList());
      List<BeanGetter> propertyGetters = getters.getOrDefault(propertyName, emptyList());
      List<BeanSetter> propertySetters = setters.getOrDefault(propertyName, emptyList());

      boolean ignored = false;

      BeanField propertyField;
      if (propertyFields.isEmpty()) {
        propertyField = null;
      } else if (propertyFields.size() == 1) {
        propertyField = propertyFields.get(0);
      } else {
        LOGGER.debug("Ignoring property {} because of multiple conflicting fields with same name");
        propertyField = null;
        ignored = true;
      }

      BeanGetter propertyGetter;
      if (propertyGetters.isEmpty()) {
        propertyGetter = null;
      } else if (propertyGetters.size() == 1) {
        propertyGetter = propertyGetters.get(0);
      } else {
        BeanGetter first = propertyGetters.get(0);
        if (propertyGetters.stream()
            .allMatch(g -> g.getGenericType().equals(first.getGenericType()))) {
          propertyGetter = first;
        } else {
          LOGGER.debug(
              "Ignoring property {} because of multiple conflicting getters with same name",
              propertyName);
          propertyGetter = null;
          ignored = true;
        }
      }

      BeanSetter propertySetter;
      if (propertySetters.isEmpty()) {
        propertySetter = null;
      } else if (propertySetters.size() == 1) {
        propertySetter = propertySetters.get(0);
      } else {
        BeanSetter first = propertySetters.get(0);
        if (propertySetters.stream()
            .allMatch(g -> g.getGenericType().equals(first.getGenericType()))) {
          propertySetter = first;
        } else {
          LOGGER.debug(
              "Ignoring property {} because of multiple conflicting setters with same name",
              propertyName);
          propertySetter = null;
          ignored = true;
        }
      }

      if (ignored == false) {
        List<BeanElement> propertyElements = new ArrayList<>(3);
        if (propertyField != null)
          propertyElements.add(propertyField);
        if (propertyGetter != null)
          propertyElements.add(propertyGetter);
        if (propertySetter != null)
          propertyElements.add(propertySetter);

        BeanElement first = propertyElements.get(0);
        if (propertyElements.stream()
            .allMatch(e -> e.getGenericType().equals(first.getGenericType()))) {
          boolean gettable =
              (propertyField != null && propertyField.isGettable()) || propertyGetter != null;
          boolean settable =
              (propertyField != null && propertyField.isSettable()) || propertySetter != null;
          if (gettable && settable) {
            result.addProperty(
                new BeanProperty(result, propertyField, propertyGetter, propertySetter));
          } else {
            LOGGER.debug("Ignoring property {} because it is not both gettable and settable",
                propertyName);
          }
        } else {
          LOGGER.debug(
              "Ignoring property {} because of conflicting types among field, getter, and setter",
              propertyName);
        }
      }
    }

    CACHE.put(rawType, result);

    return result;
  }

  private final Class<?> rawType;
  private final Constructor<?> defaultConstructor;
  private final List<BeanProperty> properties;

  /* default */ BeanClass(Class<?> rawType, Constructor<?> defaultConstructor) {
    this.rawType = rawType;
    this.defaultConstructor = defaultConstructor;
    this.properties = new ArrayList<>();
  }

  public Class<?> getRawType() {
    return rawType;
  }

  private Constructor<?> getDefaultConstructor() {
    return defaultConstructor;
  }

  public BeanInstance newInstance() throws InvocationTargetException {
    try {
      return new BeanInstance(this, getDefaultConstructor().newInstance());
    } catch (InstantiationException e) {
      // We check that the class can be instantiated. This should never happen.
      throw new AssertionError("could not instantiate class", e);
    } catch (IllegalAccessException e) {
      // We check that the class and constructor are visible. This should never happen.
      throw new AssertionError("could not instantiate class", e);
    } catch (IllegalArgumentException e) {
      // We don't give any arguments. This should never happen.
      throw new AssertionError("could not instantiate class", e);
    }
  }

  public Set<String> getPropertyNames() {
    return getProperties().stream().map(BeanProperty::getName).collect(toSet());
  }

  public Optional<BeanProperty> getProperty(String name) {
    return getProperties().stream().filter(p -> p.getName().equals(name)).findFirst();
  }

  public BeanProperty get(int index) {
    return getProperties().get(index);
  }

  public int size() {
    return getProperties().size();
  }

  public Iterator<BeanProperty> iterator() {
    return getProperties().iterator();
  }

  public Stream<BeanProperty> stream() {
    return getProperties().stream();
  }

  private List<BeanProperty> getProperties() {
    return unmodifiableList(properties);
  }

  private void addProperty(BeanProperty property) {
    if (!property.getBeanClass().equals(this))
      throw new IllegalArgumentException("property belongs to another class");
    properties.add(property);
  }
  
  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(rawType);
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
    BeanClass other = (BeanClass) obj;
    return Objects.equals(rawType, other.rawType);
  }

  @Override
  @Generated
  public String toString() {
    return "BeanClass [rawType=" + rawType + "]";
  }
}
