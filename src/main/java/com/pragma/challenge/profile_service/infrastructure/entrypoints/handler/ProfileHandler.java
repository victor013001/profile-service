package com.pragma.challenge.profile_service.infrastructure.entrypoints.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ProfileHandler {
  Mono<ServerResponse> createProfile(ServerRequest request);

  Mono<ServerResponse> getProfiles(ServerRequest request);
}
