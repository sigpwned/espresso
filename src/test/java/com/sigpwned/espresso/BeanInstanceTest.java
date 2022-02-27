package com.sigpwned.espresso;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.junit.Test;

public class BeanInstanceTest {
  public static class ExampleBean {
    private String alpha;

    public String getAlpha() {
      return alpha;
    }

    public void setAlpha(String alpha) {
      this.alpha = alpha;
    }

    public ExampleBean withAlpha(String alpha) {
      setAlpha(alpha);
      return this;
    }

    @Override
    public int hashCode() {
      return Objects.hash(alpha);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ExampleBean other = (ExampleBean) obj;
      return Objects.equals(alpha, other.alpha);
    }

    @Override
    public String toString() {
      return "ExampleBean [alpha=" + alpha + "]";
    }
  }

  @Test
  public void exampleTest() throws InvocationTargetException {
    final String alpha = "alpha";
    final String hello = "hello";

    BeanInstance instance = BeanClass.scan(ExampleBean.class).newInstance();

    instance.set(alpha, hello);

    assertThat(instance.get(alpha), is(hello));

    assertThat(instance.getInstance(), is(new ExampleBean().withAlpha(hello)));
  }

  public static class ExampleChildBean extends ExampleBean {
    private int bravo;

    public int getBravo() {
      return bravo;
    }

    public void setBravo(int bravo) {
      this.bravo = bravo;
    }

    public ExampleChildBean withBravo(int bravo) {
      setBravo(bravo);
      return this;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Objects.hash(bravo);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (!super.equals(obj))
        return false;
      if (getClass() != obj.getClass())
        return false;
      ExampleChildBean other = (ExampleChildBean) obj;
      return bravo == other.bravo;
    }

    @Override
    public String toString() {
      return "ExampleChildBean [bravo=" + bravo + "]";
    }
  }

  @Test
  public void inheritanceTest() throws InvocationTargetException {
    final String alpha = "alpha";
    final String bravo = "bravo";
    final String hello = "hello";
    final int five = 5;

    BeanInstance instance = BeanClass.scan(ExampleChildBean.class).newInstance();

    instance.set(alpha, hello);
    instance.set(bravo, five);

    assertThat(instance.get(alpha), is(hello));
    assertThat(instance.get(bravo), is(five));

    assertThat(instance.getInstance(), is(new ExampleChildBean().withBravo(five).withAlpha(hello)));
  }
  
  public static class PreferGetterTest {
    public String alpha;
    
    public String getAlpha() {
      return "bar";
    }
  }
  
  @Test
  public void preferGetterTest() throws InvocationTargetException {
    final String alpha="alpha";
    final String foo="foo";
    final String bar="bar";
    
    PreferGetterTest instance=new PreferGetterTest();
    instance.alpha = foo;
    
    assertThat(BeanInstance.wrap(instance).get(alpha), is(bar));
  }
  
  public static class PreferSetterTest {
    public String alpha;
    
    public void setAlpha(String alpha) {
      this.alpha = "bar";
    }
  }
  
  @Test
  public void preferSetterTest() throws InvocationTargetException {
    final String alpha="alpha";
    final String foo="foo";
    final String bar="bar";
    
    PreferGetterTest instance=new PreferGetterTest();
    
    BeanInstance.wrap(instance).set(alpha, foo);
    
    assertThat(instance.getAlpha(), is(bar));
  }
}
