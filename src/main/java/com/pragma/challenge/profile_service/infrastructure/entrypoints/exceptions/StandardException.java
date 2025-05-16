package com.pragma.challenge.profile_service.infrastructure.entrypoints.exceptions;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StandardException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final StandardError standardError;

  public StandardException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.standardError =
        StandardError.builder().timestamp(LocalDateTime.now()).description(message).build();
  }
}
