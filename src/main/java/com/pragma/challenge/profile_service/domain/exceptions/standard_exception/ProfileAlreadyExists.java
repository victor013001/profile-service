package com.pragma.challenge.profile_service.domain.exceptions.standard_exception;

import com.pragma.challenge.profile_service.domain.exceptions.StandardException;
import com.pragma.challenge.profile_service.domain.enums.ServerResponses;

public class ProfileAlreadyExists extends StandardException {
  public ProfileAlreadyExists() {
    super(ServerResponses.PROFILE_ALREADY_EXISTS);
  }
}
