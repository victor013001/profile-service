package com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.impl;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.BootcampProfiles;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileDto;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileIdsRequest;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.ProfileHandler;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.BootcampProfileMapper;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.DefaultServerResponseMapper;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.ProfileIdsMapper;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.ProfileMapper;
import com.pragma.challenge.profile_service.domain.constants.Constants;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.util.RequestValidator;
import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileHandlerImpl implements ProfileHandler {
  private static final String LOG_PREFIX = "[PROFILE_HANDLER] >>> ";

  private final ProfileServicePort profileServicePort;
  private final ProfileMapper profileMapper;
  private final RequestValidator requestValidator;
  private final DefaultServerResponseMapper defaultServerResponseMapper;
  private final ProfileIdsMapper profileIdsMapper;
  private final BootcampProfileMapper bootcampProfileMapper;

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
                      profile ->
                          log.info(
                              "{} {} with id: {} name: {} and description: {}.",
                              LOG_PREFIX,
                              ServerResponses.PROFILE_CREATED.getMessage(),
                              profile.id(),
                              profile.name(),
                              profile.description()));
            })
        .flatMap(
            ignore ->
                ServerResponse.status(ServerResponses.PROFILE_CREATED.getHttpStatus())
                    .bodyValue(
                        defaultServerResponseMapper.toResponse(
                            ServerResponses.PROFILE_CREATED.getMessage())));
  }

  @Override
  public Mono<ServerResponse> getProfiles(ServerRequest request) {
    String pageNumber =
        request.queryParam(Constants.PAGE_NUMBER).orElse(Constants.PAGE_NUMBER_DEFAULT);
    String pageSize = request.queryParam(Constants.PAGE_SIZE).orElse(Constants.PAGE_SIZE_DEFAULT);
    String sortDirectionParam =
        request.queryParam(Constants.SORT_DIRECTION).orElse(Constants.ASC_PARAM);
    String sortByParam = request.queryParam(Constants.SORT_BY).orElse(Constants.NAME_PARAM);
    log.info(
        "{} Getting profiles sorted by: {} with direction: {}",
        LOG_PREFIX,
        sortByParam,
        sortDirectionParam);
    return profileServicePort
        .getProfiles(
            PageRequest.of(
                requestValidator.toInt(pageNumber),
                requestValidator.toInt(pageSize),
                Sort.by(
                    requestValidator.toSortDirection(sortDirectionParam),
                    requestValidator.validate(sortByParam))))
        .collectList()
        .flatMap(
            profileTechnologies -> {
              log.info("{} Profiles page: {} with size: {}", LOG_PREFIX, pageNumber, pageSize);
              return ServerResponse.status(HttpStatus.OK)
                  .bodyValue(defaultServerResponseMapper.toResponse(profileTechnologies));
            });
  }

  @Override
  public Mono<ServerResponse> profileExists(ServerRequest request) {
    ProfileIdsRequest profileIdsRequest = new ProfileIdsRequest(request.queryParams().get("id"));
    return Mono.just(profileIdsMapper.toProfileIdsDto(profileIdsRequest))
        .flatMap(requestValidator::validate)
        .flatMap(
            profileIdsDto -> {
              log.info(
                  "{} Checking if profiles with ids {} exist.", LOG_PREFIX, profileIdsDto.ids());
              return profileServicePort.checkProfileIds(
                  profileIdsMapper.toProfileIds(profileIdsDto));
            })
        .flatMap(
            exists ->
                ServerResponse.status(HttpStatus.OK)
                    .bodyValue(defaultServerResponseMapper.toResponse(exists)));
  }

  @Override
  public Mono<ServerResponse> createRelation(ServerRequest request) {
    return request
        .bodyToMono(BootcampProfiles.class)
        .flatMap(requestValidator::validate)
        .flatMap(
            bootcampProfileDto -> {
              log.info("{} Creating bootcamp profile relations", LOG_PREFIX);
              return profileServicePort
                  .registerBootcampProfileRelation(
                      bootcampProfileMapper.toBootcampProfile(
                          bootcampProfileDto.relations()))
                  .doOnSuccess(
                      technology -> log.info("{} Relations created successfully.", LOG_PREFIX));
            })
        .then(
            ServerResponse.status(ServerResponses.BOOTCAMP_PROFILE_CREATED.getHttpStatus())
                .bodyValue(
                    defaultServerResponseMapper.toResponse(
                        ServerResponses.BOOTCAMP_PROFILE_CREATED.getMessage())));
  }
}
