# ESPRESSO

Espresso is a library of streamlined Java Beans for Java version 8 or higher.

## Goals

* Expose a simple, straightforward way to manipulate Java beans

## Non-Goals

* Create a general Java beans framework other libraries can use to manipulate Java Beans, e.g. [Java Beans](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/beans/Beans.html) and [Apache Commons BeanUtils](https://commons.apache.org/proper/commons-beanutils/)
* Create a general reflections framework for assisting in reflecitons tasks, e.g. [reflections](https://github.com/ronmamo/reflections)

## Usage

### Scan a class

    BeanClass bc;
    try {
        bc = BeanClass.scan(Example.class);
        // The given class was successfully parsed into a bean class!
    }
    catch(IllegalArgumentException e) {
        // The given class cannot be parsed as a bean class.
    }

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

## Colophon

[Espresso](https://en.wikipedia.org/wiki/Espresso) is a method for brewing delicious, high-caffeine coffee from a variety of different types of coffee beans.