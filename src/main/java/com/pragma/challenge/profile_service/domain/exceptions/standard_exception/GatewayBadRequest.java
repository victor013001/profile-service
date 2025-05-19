package com.pragma.challenge.profile_service.domain.exceptions.standard_exception;

import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import com.pragma.challenge.profile_service.domain.exceptions.StandardException;

public class GatewayBadRequest extends StandardException {
  public GatewayBadRequest() {
    super(ServerResponses.GATEWAY_BAD_REQUEST);
  }
}
