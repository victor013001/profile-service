package com.pragma.challenge.profile_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerResponses {
  BAD_REQUEST(
      "E000",
      HttpStatus.BAD_REQUEST,
      "The request could not be processed due to invalid or incomplete data."),
  SERVER_ERROR(
      "E001",
      HttpStatus.INTERNAL_SERVER_ERROR,
      "An unexpected server error occurred. Please try again later."),
  RESOURCE_NOT_FOUND("E002", HttpStatus.NOT_FOUND, "The requested resource was not found."),
  PROFILE_CREATED("", HttpStatus.CREATED, "The profile was created successfully."),
  PROFILE_ALREADY_EXISTS("E003", HttpStatus.CONFLICT, "The technology could not be created due to a data conflict.");

  private final String code;
  private final HttpStatus httpStatus;
  private final String message;
}
