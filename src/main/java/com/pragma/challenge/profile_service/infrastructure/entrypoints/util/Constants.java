package com.pragma.challenge.profile_service.infrastructure.entrypoints.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {
  public static final String PAGE_NUMBER = "pageNumber";
  public static final String PAGE_SIZE = "pageSize";
  public static final String SORT_DIRECTION = "sortDirection";
  public static final String SORT_BY = "sortBy";
  public static final String NAME_PARAM = "name";
  public static final String ID_PARAM = "id";
  public static final String PROFILE_ID_PARAM = "profileId";
  public static final String TECHNOLOGY_PARAM = "technology";
  public static final String ASC_PARAM = "asc";
  public static final String PAGE_NUMBER_DEFAULT = "0";
  public static final String PAGE_SIZE_DEFAULT = "10";
  public static final List<String> SORT_BY_OPTIONS = List.of(NAME_PARAM, TECHNOLOGY_PARAM);
}
