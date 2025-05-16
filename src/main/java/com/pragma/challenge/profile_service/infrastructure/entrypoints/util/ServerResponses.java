package com.pragma.challenge.profile_service.infrastructure.entrypoints.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerResponses {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "Unable to process the request with the given data."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred on the server."),
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found."),
  PROFILE_CREATED(HttpStatus.CREATED, "Profile created successfully."),
  PROFILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Conflict with the given data.");

  private final HttpStatus httpStatus;
  private final String message;
}
