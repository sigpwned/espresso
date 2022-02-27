package com.sigpwned.espresso;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class BeanClassTest {
  public static class SmokeTest {
    public int x;
  }

  /**
   * We should detect a single property called x
   */
  @Test
  public void smokeTest() {
    BeanClass bc = BeanClass.scan(SmokeTest.class);

    assertThat(bc.getPropertyNames(), is(singleton("x")));
  }

  public static class ScanPublicFieldWithoutGetterWithoutSetterTest {
    public int x;
  }

  /**
   * We should detect a single public field
   */
  @Test
  public void scanPublicFieldWithoutGetterWithoutSetterTest() {
    BeanClass bc = BeanClass.scan(ScanPublicFieldWithoutGetterWithoutSetterTest.class);

    assertThat(bc.size(), is(1));

    BeanProperty x = bc.getProperty("x").get();

    assertThat(x.getName(), is("x"));
    assertThat(x.getGenericType(), is((Type) int.class));
  }

  public static class ScanPublicStaticFieldWithoutGetterWithoutSetterTestClass {
    public static int x;
  }

  /**
   * We should not detect a single public field
   */
  @Test
  public void scanPublicStaticFieldWithoutGetterWithoutSetterTest() {
    BeanClass bc = BeanClass.scan(ScanPublicStaticFieldWithoutGetterWithoutSetterTestClass.class);
    assertThat(bc.size(), is(0));
  }

  public static class ScanPublicFinalFieldWithoutGetterWithoutSetterTest {
    public final int x = 0;
  }

  /**
   * We should not detect a single public final field
   */
  @Test
  public void scanPublicFinalFieldWithoutGetterWithoutSetterTest() {
    BeanClass bc = BeanClass.scan(ScanPublicFinalFieldWithoutGetterWithoutSetterTest.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanPrivateFieldWithoutGetterWithoutSetterTest {
    @SuppressWarnings("unused")
    private int x;
  }

  /**
   * We should not detect a single private field because it is not both settable and gettable
   */
  @Test
  public void scanPrivateFieldWithoutGetterWithoutSetterTest() {
    BeanClass bc = BeanClass.scan(ScanPrivateFieldWithoutGetterWithoutSetterTest.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithoutFieldWithGetterWithoutSetterTest {
    public int getX() {
      return 0;
    }
  }

  /**
   * We should not detect a standalone getter because it's not settable
   */
  @Test
  public void scanWithoutFieldWithGetterWithoutSetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithoutFieldWithGetterWithoutSetterTest.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithoutFieldWithoutGetterWithSetterTest {
    public void setX(int x) {}
  }

  /**
   * We should not detect a standalone setter because it's not gettable
   */
  @Test
  public void scanWithoutFieldWithoutGetterWithSetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithoutFieldWithoutGetterWithSetterTest.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithoutFieldWithGetterWithSetterTest {
    public int getX() {
      return 0;
    }

    public void setX(int x) {}
  }

  /**
   * We should detect a getter/setter pair
   */
  @Test
  public void scanWithoutFieldWithGetterWithSetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithoutFieldWithGetterWithSetterTest.class);

    assertThat(bc.size(), is(1));

    BeanProperty x = bc.getProperty("x").get();

    assertThat(x.getName(), is("x"));
    assertThat(x.getGenericType(), is((Type) int.class));
  }

  public static class ScanWithoutFieldWithGetterWithSetterTypeMismatchTest {
    public int getX() {
      return 0;
    }

    public void setX(long x) {}
  }

  /**
   * We should not detect a getter/setter pair with different types
   */
  @Test
  public void scanWithoutFieldWithGetterWithSetterTypeMismatchTest() {
    BeanClass bc = BeanClass.scan(ScanWithoutFieldWithGetterWithSetterTypeMismatchTest.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithFieldWithGetterWithSetterTest {
    private int x;

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }
  }

  /**
   * We should detect a getter/setter pair with a matching field
   */
  @Test
  public void scanWithFieldWithGetterWithSetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithFieldWithGetterWithSetterTest.class);

    assertThat(bc.size(), is(1));

    BeanProperty x = bc.getProperty("x").get();

    assertThat(x.getName(), is("x"));
    assertThat(x.getGenericType(), is((Type) int.class));
  }

  public static class ScanWithFieldWithGetterWithSetterTypeMismatchTest {
    private long x;

    public int getX() {
      return (int) x;
    }

    public void setX(int x) {
      this.x = x;
    }
  }

  /**
   * We should not detect a getter/setter pair with a matching field and mismatched types
   */
  @Test
  public void scanWithFieldWithGetterWithSetterTypeMismatchTest() {
    BeanClass bc = BeanClass.scan(ScanWithFieldWithGetterWithSetterTypeMismatchTest.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanMultipleFieldsInOneClassTest {
    private int x;

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }

    public String y;
  }

  /**
   * We should detect multiple fields
   */
  @Test
  public void scanMultipleFieldsInOneClassTest() {
    BeanClass bc = BeanClass.scan(ScanMultipleFieldsInOneClassTest.class);

    assertThat(bc.size(), is(2));

    BeanProperty x = bc.getProperty("x").get();
    assertThat(x.getName(), is("x"));
    assertThat(x.getGenericType(), is((Type) int.class));

    BeanProperty y = bc.getProperty("y").get();
    assertThat(y.getName(), is("y"));
    assertThat(y.getGenericType(), is((Type) String.class));
  }

  public static class ScanMultipleFieldsInMultipleClassesTestParent {
    private int x;

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }
  }

  public static class ScanMultipleFieldsInMultipleClassesTestChild
      extends ScanMultipleFieldsInMultipleClassesTestParent {
    public String y;
  }

  /**
   * We should detect multiple fields
   */
  @Test
  public void scanMultipleFieldsInMultipleClassesTest() {
    BeanClass bc = BeanClass.scan(ScanMultipleFieldsInMultipleClassesTestChild.class);

    assertThat(bc.size(), is(2));

    BeanProperty x = bc.getProperty("x").get();
    assertThat(x.getName(), is("x"));
    assertThat(x.getGenericType(), is((Type) int.class));

    BeanProperty y = bc.getProperty("y").get();
    assertThat(y.getName(), is("y"));
    assertThat(y.getGenericType(), is((Type) String.class));
  }

  /**
   * We should fail to scan a primitive class
   */
  @Test(expected = IllegalArgumentException.class)
  public void scanPrimitiveTest() {
    BeanClass.scan(int.class);
  }

  /**
   * We should fail to scan the void class
   */
  @Test(expected = IllegalArgumentException.class)
  public void scanVoidTest() {
    BeanClass.scan(void.class);
  }

  /**
   * We should fail to scan an array class
   */
  @Test(expected = IllegalArgumentException.class)
  public void scanArrayTest() {
    BeanClass.scan(int[].class);
  }

  public static abstract class ScanAbstractTest {
    public int x;
  }

  /**
   * We should fail to scan an abstract class
   */
  @Test(expected = IllegalArgumentException.class)
  public void scanAbstractTest() {
    BeanClass.scan(ScanAbstractTest.class);
  }

  public static class ScanWithoutDefaultConstructorTest {
    public ScanWithoutDefaultConstructorTest(int x) {
      this.x = x;
    }

    public int x;
  }

  /**
   * We should fail to scan an abstract class
   */
  @Test(expected = IllegalArgumentException.class)
  public void scanWithoutDefaultConstructorTest() {
    BeanClass.scan(ScanWithoutDefaultConstructorTest.class);
  }

  public static class ScanWithPrivateDefaultConstructorTest {
    private ScanWithPrivateDefaultConstructorTest(int x) {
      this.x = x;
    }

    public int x;
  }

  /**
   * We should fail to scan an abstract class
   */
  @Test(expected = IllegalArgumentException.class)
  public void scanWithPirvateDefaultConstructorTest() {
    BeanClass.scan(ScanWithPrivateDefaultConstructorTest.class);
  }

  public static class ScanWithFieldHidingTestParent {
    public int x;
  }

  public static class ScanWithFieldHidingTestChild extends ScanWithFieldHidingTestParent {
    public int x;
  }

  /**
   * We should fail to scan in a field with multiple definitions.
   */
  @Test
  public void scanWithFieldHidingTest() {
    BeanClass bc = BeanClass.scan(ScanWithFieldHidingTestChild.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithCovariantGetterTestParent {
    public Number foo;

    public Number getFoo() {
      return null;
    }
  }

  public static class ScanWithCovariantGetterTestChild extends ScanWithCovariantGetterTestParent {
    @Override
    public Integer getFoo() {
      return null;
    }
  }

  /**
   * We should fail to scan in a field with multiple definitions.
   */
  @Test
  public void scanWithCovariantGetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithCovariantGetterTestChild.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithOverrideGetterTestParent {
    public Number foo;

    public Number getFoo() {
      return null;
    }
  }

  public static class ScanWithOverrideGetterTestChild extends ScanWithOverrideGetterTestParent {
    @Override
    public Number getFoo() {
      return null;
    }
  }

  /**
   * We should fail to scan in a field with multiple definitions.
   */
  @Test
  public void scanWithOverrideGetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithOverrideGetterTestChild.class);

    assertThat(bc.size(), is(1));

    BeanProperty x = bc.getProperty("foo").get();
    assertThat(x.getName(), is("foo"));
    assertThat(x.getGenericType(), is((Type) Number.class));
  }

  public static class ScanWithCovariantSetterTestParent {
    public Number foo;

    public void setFoo(Number foo) {}
  }

  public static class ScanWithCovariantSetterTestChild extends ScanWithCovariantSetterTestParent {
    public void setFoo(Integer foo) {}
  }

  /**
   * We should fail to scan in a field with multiple definitions.
   */
  @Test
  public void scanWithCovariantSetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithCovariantSetterTestChild.class);

    assertThat(bc.size(), is(0));
  }

  public static class ScanWithOverrideSetterTestParent {
    public Number foo;

    public void setFoo(Number foo) {}
  }

  public static class ScanWithOverrideSetterTestChild extends ScanWithOverrideSetterTestParent {
    @Override
    public void setFoo(Number foo) {}
  }

  /**
   * We should fail to scan in a field with multiple definitions.
   */
  @Test
  public void scanWithOverrideSetterTest() {
    BeanClass bc = BeanClass.scan(ScanWithOverrideSetterTestChild.class);

    assertThat(bc.size(), is(1));

    BeanProperty x = bc.getProperty("foo").get();
    assertThat(x.getName(), is("foo"));
    assertThat(x.getGenericType(), is((Type) Number.class));
  }

  public static class Example {
    public int x;
  }

  @Test
  public void iteratorTest() {
    BeanClass bc = BeanClass.scan(Example.class);

    Set<String> names = new HashSet<>();
    for (BeanProperty p : bc)
      names.add(p.getName());

    assertThat(names, is(singleton("x")));
  }

  @Test
  public void streamTest() {
    BeanClass bc = BeanClass.scan(Example.class);

    Set<String> names = bc.stream().map(BeanProperty::getName).collect(toSet());

    assertThat(names, is(singleton("x")));
  }

  @Test
  public void getTest() {
    BeanClass bc = BeanClass.scan(Example.class);

    Set<String> names = new HashSet<>();
    for (int i = 0; i < bc.size(); i++)
      names.add(bc.get(i).getName());

    assertThat(names, is(singleton("x")));
  }

  @Test
  public void rawTypeTest() {
    BeanClass bc = BeanClass.scan(Example.class);

    assertThat(bc.getRawType().getName(), is(Example.class.getName()));
  }

  public static class BooleanExample {
    public boolean foo = true;

    public boolean isFoo() {
      return false;
    }
  }

  @Test
  public void booleanIsGetterTest() {
    BeanClass bc = BeanClass.scan(BooleanExample.class);
    
    assertThat(bc.size(), is(1));

    BeanProperty foo = bc.getProperty("foo").get();
    assertThat(foo.getName(), is("foo"));
    assertThat(foo.getGenericType(), is((Type) boolean.class));
  }

  /**
   * We should return a equals object for the same class if not in the cache
   */
  @Test
  public void cacheBustTest() {
    BeanClass scan1 = BeanClass.scan(SmokeTest.class);

    BeanClass.CACHE.clear();

    BeanClass scan2 = BeanClass.scan(SmokeTest.class);

    assertThat(scan2, is(scan1));
  }

  /**
   * We should return a equals object for the same class if in the cache
   */
  @Test
  public void cacheTest() {
    BeanClass.CACHE.clear();

    BeanClass scan1 = BeanClass.scan(SmokeTest.class);

    BeanClass scan2 = BeanClass.scan(SmokeTest.class);

    assertThat(scan2, is(scan1));
  }
}
