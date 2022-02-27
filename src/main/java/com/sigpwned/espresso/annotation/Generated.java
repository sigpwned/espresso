package com.sigpwned.espresso.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks generated code for exclusion from code coverage reports 
 */
@Retention(CLASS)
@Target({TYPE, METHOD})
public @interface Generated {

}
