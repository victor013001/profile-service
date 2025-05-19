package com.pragma.challenge.profile_service.domain.exceptions.standard_exception;

import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import com.pragma.challenge.profile_service.domain.exceptions.StandardException;

public class TechnologiesNotFound extends StandardException {
  public TechnologiesNotFound() {
    super(ServerResponses.TECHNOLOGIES_NOT_FOUND);
  }
}
