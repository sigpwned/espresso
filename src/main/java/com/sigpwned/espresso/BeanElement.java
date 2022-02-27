package com.sigpwned.espresso;

import java.lang.reflect.Type;

public interface BeanElement {
  public String getName();
  
  public Type getGenericType();
}
