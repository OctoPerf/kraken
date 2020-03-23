package com.kraken.tools.obfuscation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Any class annotated with this annotation is excluded from
 * obfuscation renaming.
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface ExcludeFromObfuscation {
  String value() default "";
}
