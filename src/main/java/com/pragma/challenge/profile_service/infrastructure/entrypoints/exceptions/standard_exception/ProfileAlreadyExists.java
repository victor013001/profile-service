package com.pragma.challenge.profile_service.infrastructure.entrypoints.exceptions.standard_exception;

import com.pragma.challenge.profile_service.infrastructure.entrypoints.exceptions.StandardException;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.util.ServerResponses;

public class ProfileAlreadyExists extends StandardException {
  public ProfileAlreadyExists() {
    super(
        ServerResponses.PROFILE_ALREADY_EXISTS.getHttpStatus(),
        ServerResponses.PROFILE_ALREADY_EXISTS.getMessage());
  }
}
