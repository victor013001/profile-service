package com.pragma.challenge.profile_service.domain.constants;

import java.util.List;
import lombok.experimental.UtilityClass;

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
  public static final String BAD_REQUEST_MSG = "The request could not be processed due to invalid or incomplete data.";
  public static final String SERVER_ERROR_MSG = "An unexpected server error occurred. Please try again later.";
  public static final String RESOURCE_NOT_FOUND_MSG = "The requested resource was not found.";
  public static final String PROFILE_CREATED_MSG = "The profile was created successfully.";
  public static final String PROFILE_ALREADY_EXISTS_MSG = "The technology could not be created due to a data conflict.";
  public static final String GATEWAY_ERROR_MSG = "Failed to process the request due to an internal gateway error.";
  public static final String GATEWAY_BAD_REQUEST_MSG = "An unexpected error occurred while processing the request through the gateway.";
  public static final String TECHNOLOGIES_NOT_FOUND_MSG = "Technologies provided not found.";
  public static final String BOOTCAMP_PROFILE_CREATED_MSG = "Relations created successfully.";
  public static final String PROFILES_DELETED_MSG = "The profiles of the bootcamp were deleted successfully.";
  public static final String ID_PATH_VARIABLE = "id";
}
