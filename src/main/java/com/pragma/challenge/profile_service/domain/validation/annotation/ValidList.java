package com.pragma.challenge.profile_service.domain.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidList {
  boolean notNull() default true;

  boolean notEmpty() default true;

  int min() default -1;

  int max() default -1;
}
