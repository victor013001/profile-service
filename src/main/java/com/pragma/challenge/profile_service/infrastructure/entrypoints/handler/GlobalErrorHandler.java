package com.pragma.challenge.profile_service.infrastructure.entrypoints.handler;

import java.time.LocalDateTime;

import com.pragma.challenge.profile_service.infrastructure.entrypoints.exceptions.StandardError;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.exceptions.StandardException;
import com.pragma.challenge.profile_service.infrastructure.util.DefaultServerResponseContext;
import com.pragma.challenge.profile_service.infrastructure.util.ServerResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-2)
@Component
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {
  private static final String LOG_PREFIX = "[ERROR_HANDLER] >>> ";

  private final DefaultServerResponseContext serverResponseContext;

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    log.info(
        "{} Exception {} caught. Caused by: {}",
        LOG_PREFIX,
        ex.getClass().getSimpleName(),
        ex.getMessage());

    if (ex instanceof StandardException standardException) {
      return writeExchangeResponse(
          exchange, standardException.getHttpStatus(), standardException.getStandardError());
    }

    return writeExchangeResponse(
        exchange,
        ServerResponses.SERVER_ERROR.getHttpStatus(),
        StandardError.builder()
            .description(ServerResponses.SERVER_ERROR.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
  }

  private Mono<Void> writeExchangeResponse(
      ServerWebExchange exchange, HttpStatus httpStatus, StandardError standardError) {
    return ServerResponse.status(httpStatus)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(standardError)
        .flatMap(serverResponse -> serverResponse.writeTo(exchange, serverResponseContext));
  }
}
