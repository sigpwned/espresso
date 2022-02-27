# ESPRESSO [![tests](https://github.com/sigpwned/espresso/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/espresso/actions/workflows/tests.yml) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_espresso&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sigpwned_espresso) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_espresso&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_espresso) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_espresso&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_espresso)

Espresso simple Java Beans library for version 8 or higher.

## Goals

Provide a high-level library that makes it easy to:

* Scan Java bean classes for metadata
* Create new instances of Java Beans
* Manipulate existing instances of Java beans

## Non-Goals

* Create a low-level library to help applications roll their own bean processing capabilities. Plenty of libraries already do that well, e.g. [Java Beans](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/beans/Beans.html) and [Apache Commons BeanUtils](https://commons.apache.org/proper/commons-beanutils/).
* Create a general-purpose reflections library for assisting in reflecitons tasks. Plenty of libraries already do that well, e.g. [reflections](https://github.com/ronmamo/reflections).

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