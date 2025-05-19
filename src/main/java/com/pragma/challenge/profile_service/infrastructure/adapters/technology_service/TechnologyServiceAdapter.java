package com.pragma.challenge.profile_service.infrastructure.adapters.technology_service;

import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.GatewayBadRequest;
import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.GatewayError;
import com.pragma.challenge.profile_service.domain.spi.TechnologyServiceGateway;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.TechnologyProfileDto;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TechnologyServiceAdapter implements TechnologyServiceGateway {
  private static final String LOG_PREFIX = "[TECHNOLOGY_SERVICE_GATEWAY] >>>";

  private final WebClient webClient;
  private final Retry retry;
  private final Bulkhead bulkhead;

  @Override
  @CircuitBreaker(name = "technologyService", fallbackMethod = "fallback")
  public Mono<Boolean> technologiesExists(List<Long> technologiesId) {
    log.info(
        "{} Starting technologies validation process for ids: {}.", LOG_PREFIX, technologiesId);
    return webClient
        .get()
        .uri(
            uriBuilder -> {
              UriBuilder builder = uriBuilder.path("api/v1/technology/exists");
              technologiesId.forEach(id -> builder.queryParam("id", id));
              return builder.build();
            })
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(GatewayBadRequest::new))
        .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(GatewayError::new))
        .bodyToMono(new ParameterizedTypeReference<DefaultServerResponse<Boolean>>() {})
        .map(DefaultServerResponse::data)
        .doOnNext(exists -> log.info("{} Received API response.", LOG_PREFIX))
        .transformDeferred(RetryOperator.of(retry))
        .transformDeferred(mono -> Mono.defer(() -> bulkhead.executeSupplier(() -> mono)))
        .doOnTerminate(
            () ->
                log.info(
                    "{} Completed technologies validation process in Technology Service.",
                    LOG_PREFIX))
        .doOnError(
            ignore ->
                log.error(
                    "{} Error calling Technology Service with ids: {}",
                    LOG_PREFIX,
                    technologiesId));
  }

  @Override
  @CircuitBreaker(name = "technologyService", fallbackMethod = "fallback")
  public Mono<Void> createRelation(TechnologyProfileDto technologyProfileDto) {
    log.info("{} Starting technologies and profile relation process.", LOG_PREFIX);
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("api/v1/technology/profile").build())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(technologyProfileDto)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(GatewayBadRequest::new))
        .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(GatewayError::new))
        .bodyToMono(new ParameterizedTypeReference<DefaultServerResponse<String>>() {})
        .map(DefaultServerResponse::data)
        .doOnNext(response -> log.info("{} Received API response: {}", LOG_PREFIX, response))
        .transformDeferred(
            mono -> mono.retryWhen(reactor.util.retry.Retry.fixedDelay(3, Duration.ofSeconds(2))))
        .transformDeferred(mono -> Mono.defer(() -> bulkhead.executeSupplier(() -> mono)))
        .doOnTerminate(
            () ->
                log.info(
                    "{} Completed technologies and profile relation process in Technology Service.",
                    LOG_PREFIX))
        .doOnError(error -> log.error("{} Error details: {}", LOG_PREFIX, error.getMessage()))
        .then();
  }

  public Mono<Boolean> fallback(Throwable t) {
    log.warn("{} Fallback triggered for technologyService", LOG_PREFIX);
    return Mono.defer(() -> Mono.justOrEmpty(t instanceof TimeoutException ? Boolean.FALSE : null))
        .switchIfEmpty(Mono.error(t));
  }
}
