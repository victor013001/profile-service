package com.pragma.challenge.profile_service.infrastructure.entrypoints.util;

import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import com.pragma.challenge.profile_service.domain.exceptions.StandardError;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.DefaultServerResponseMapper;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class ResponseUtil {
  public static Mono<ServerResponse> buildResponse(
      HttpStatus httpStatus,
      Object data,
      StandardError error,
      DefaultServerResponseMapper responseMapper) {
    return Mono.defer(
        () ->
            ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseMapper.toResponse(data, error)));
  }

  public static StandardError buildStandardError(ServerResponses serverResponses) {
    return StandardError.builder()
        .code(serverResponses.getCode())
        .description(serverResponses.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }
}
