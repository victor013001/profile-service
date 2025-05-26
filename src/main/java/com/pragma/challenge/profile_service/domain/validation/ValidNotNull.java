package com.pragma.challenge.profile_service.domain.validation;

import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.profile_service.domain.validation.annotation.NotNull;
import java.lang.reflect.Field;
import java.util.Objects;

public class ValidNotNull {
  public static void valid(Object object) {
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      if (field.isAnnotationPresent(NotNull.class)) {
        try {
          Object value = field.get(object);
          if (Objects.isNull(value)) {
            throw new BadRequest();
          }
        } catch (IllegalAccessException ignore) {
        }
      }
    }
  }
}
