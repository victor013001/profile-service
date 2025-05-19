package com.pragma.challenge.profile_service.domain.exceptions.standard_exception;

import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import com.pragma.challenge.profile_service.domain.exceptions.StandardException;

public class GatewayError extends StandardException {
  public GatewayError() {
    super(ServerResponses.GATEWAY_ERROR);
  }
}
