package com.pragma.challenge.profile_service.infrastructure.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerResponses {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "Unable to process the request with the given data."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred on the server.");

  private final HttpStatus httpStatus;
  private final String message;
}
