package com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.impl;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileDto;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.ProfileHandler;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.ProfileMapper;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.util.RequestValidator;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.util.ServerResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileHandlerImpl implements ProfileHandler {
  private static final String LOG_PREFIX = "[Profile_Handler] >>> ";

  private final ProfileServicePort profileServicePort;
  private final ProfileMapper profileMapper;
  private final RequestValidator requestValidator;

  @Override
  public Mono<ServerResponse> createProfile(ServerRequest request) {
    return request
        .bodyToMono(ProfileDto.class)
        .flatMap(requestValidator::validate)
        .flatMap(
            profileDto -> {
              log.info(
                  "{} Creating profile with name: {}, description: {} and technologies id: {}.",
                  LOG_PREFIX,
                  profileDto.name(),
                  profileDto.description(),
                  profileDto.technologiesId());
              return profileServicePort
                  .registerProfile(profileMapper.toProfile(profileDto))
                  .doOnSuccess(
                      profile -> {
                        log.info(
                            "{} {} with name: {} and description: {}.",
                            LOG_PREFIX,
                            ServerResponses.PROFILE_CREATED.getMessage(),
                            profile.name(),
                            profile.description());
                      });
            })
        .flatMap(
            ignore ->
                ServerResponse.status(ServerResponses.PROFILE_CREATED.getHttpStatus())
                    .bodyValue(ServerResponses.PROFILE_CREATED.getMessage()));
  }
}
