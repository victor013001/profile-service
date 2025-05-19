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
  PROFILE_ALREADY_EXISTS(
      "E003", HttpStatus.CONFLICT, "The technology could not be created due to a data conflict."),
  GATEWAY_ERROR(
      "E004",
      HttpStatus.INTERNAL_SERVER_ERROR,
      "Failed to process the request due to an internal gateway error."),
  GATEWAY_BAD_REQUEST(
      "E005",
      HttpStatus.BAD_REQUEST,
      "An unexpected error occurred while processing the request through the gateway."),
  TECHNOLOGIES_NOT_FOUND("E006", HttpStatus.NOT_FOUND, "Technologies provided not found.");

  private final String code;
  private final HttpStatus httpStatus;
  private final String message;
}
