# ESPRESSO [![Tests](https://github.com/sigpwned/espresso/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/espresso/actions/workflows/tests.yml) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_espresso&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_espresso) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_espresso&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_espresso) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_espresso&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_espresso)

Espresso is a streamlined JavaBean library for version 8 or higher.

## Goals

Provide a high-level library that makes it easy to:

* Scan JavaBean classes for metadata
* Create new instances of JavaBeans
* Manipulate existing instances of JavaBeans

## Non-Goals

* Create a low-level library to help applications roll their own JavaBean processing capabilities. Plenty of libraries already do that well, e.g. [Java Beans](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/beans/Beans.html) and [Apache Commons BeanUtils](https://commons.apache.org/proper/commons-beanutils/).
* Create a general-purpose reflections library for assisting in reflections tasks. Plenty of libraries already do that well, e.g. [reflections](https://github.com/ronmamo/reflections).

## What is a JavaBean?

A JavaBean is a Java class that adheres to [the JavaBean spec](https://www.oracle.com/java/technologies/javase/javabeans-spec.html). In [short](https://stackoverflow.com/questions/3295496/what-is-a-javabean-exactly/3295517#3295517):

* All properties are private (use getters/setters)
* A public no-argument constructor
* Implements Serializable.

## How does Espresso implement the JavaBean spec?

Espresso's implementation is generally faithful, but relaxes a few constraints. In Espresso:

* Only instance properties are allowed. All static fields and methods are ignored.
* Properties are not required to have precisely a private field and public getter/setter pair. Instead, a property may consist of any of the following:
  * A private field with a public getter/setter pair
  * A public field with or without a public getter and/or setter
  * A public getter/setter pair with no field
* Classes are not requried to implement Serializable.

Espresso adds the following limitations out of practicality:

* Classes must be instantiable (so, no abstract classes, interfaces, etc.).
* Fields that hide or are hidden by another field in a superclass are ignored to avoid ambiguity.
* Getters with multiple covariant signatures are ignored to avoid ambiguity. Method overriding with the same signature is allowed and respected.
* Setters with multiple covariant signatures are ignored to avoid ambiguity. Method overriding with the same signature is allowed and respected.

## Usage

### Scan a class

    BeanClass bc;
    try {
        bc = BeanClass.scan(Example.class);
    }
    catch(IllegalArgumentException e) {
        // The given class cannot be parsed as a bean class.
	throw e;
    }

    for(BeanProperty p : bc)
      System.out.println("Example has a property named "+p.getName()+" of type "+p.getGenericType());

### Create and manipulate a bean instance

    class Example {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    BeanClass bc=BeanClass.scan(Example.class);

    BeanInstance instance=bc.newInstance();

    instance.set("value", "hello");

    Example example=(Example) instance.getInstance();

    System.out.println(example.getValue()); // prints "hello"

### Manipulate an existing bean instance

    class Example {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    BeanClass bc=BeanClass.scan(Example.class);

    Example example=new Example();

    BeanInstance instance=BeanInstance.wrap(example);

    instance.set("value", "hello");

    System.out.println(example.getValue()); // prints "hello"

## Colophon

[Espresso](https://en.wikipedia.org/wiki/Espresso) is a method for brewing delicious, high-caffeine coffee from a variety of different types of coffee beans.