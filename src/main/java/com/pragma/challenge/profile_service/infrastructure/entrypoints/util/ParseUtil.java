package com.pragma.challenge.profile_service.infrastructure.entrypoints.util;

import com.pragma.challenge.profile_service.domain.constants.Constants;
import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.BadRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class ParseUtil {

  public static Sort.Direction toSortDirection(String sortDirection) {
    try {
      return Sort.Direction.fromString(sortDirection);
    } catch (IllegalArgumentException e) {
      throw new BadRequest();
    }
  }

  public static String validate(String sortBy) {
    if (!Constants.SORT_BY_OPTIONS.contains(sortBy)) {
      throw new BadRequest();
    }
    return sortBy;
  }

  public static int toInt(String value) {
    try {
      int parsed = Integer.parseInt(value);
      if (parsed < 0) {
        throw new BadRequest();
      }
      return parsed;
    } catch (NumberFormatException e) {
      throw new BadRequest();
    }
  }

  public static long toLong(String value) {
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new BadRequest();
    }
  }
}
