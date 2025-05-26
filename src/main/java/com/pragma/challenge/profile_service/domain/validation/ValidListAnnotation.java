package com.pragma.challenge.profile_service.domain.validation;

import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.profile_service.domain.validation.annotation.ValidList;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class ValidListAnnotation {
  public static void valid(Object object) {
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      if (field.isAnnotationPresent(ValidList.class)) {
        try {
          Object value = field.get(object);
          ValidList annotation = field.getAnnotation(ValidList.class);
          if (Objects.isNull(annotation)) continue;
          if (annotation.notNull() && value == null) {
            throw new BadRequest();
          }
          if (value instanceof List<?> list) {
            if (annotation.notEmpty() && list.isEmpty()) throw new BadRequest();
            if (annotation.min() >= 0 && list.size() < annotation.min()) throw new BadRequest();
            if (annotation.max() >= 0 && list.size() > annotation.max()) throw new BadRequest();
          } else {
            throw new IllegalArgumentException("The field is not a list.");
          }
        } catch (IllegalAccessException ignore) {
        }
      }
    }
  }
}
