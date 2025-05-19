package com.pragma.challenge.profile_service.domain.usecase;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.TechnologiesNotFound;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import com.pragma.challenge.profile_service.domain.spi.TechnologyServiceGateway;
import com.pragma.challenge.profile_service.domain.mapper.ProfileTechnologyMapper;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileDto;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileRelation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileUserCase implements ProfileServicePort {
  private static final String LOG_PREFIX = "[PROFILE_USE_CASE] >>>";

  private final ProfilePersistencePort profilePersistencePort;
  private final TechnologyServiceGateway technologyServiceGateway;
  private final TransactionalOperator transactionalOperator;
  private final ProfileTechnologyMapper profileTechnologyMapper;

  @Override
  public Mono<Profile> registerProfile(Profile profile) {
    return profilePersistencePort
        .validName(profile.name())
        .then(Mono.defer(() -> registerWithTechnologies(profile)))
        .as(transactionalOperator::transactional);
  }

  @Override
  public Flux<ProfileTechnology> getProfiles(PageRequest pageRequest) {
    return profilePersistencePort
        .findAllBy(pageRequest)
        .flatMap(
            profileTechnology ->
                technologyServiceGateway
                    .getTechnologies(profileTechnology.id())
                    .map(
                        technologies ->
                            profileTechnologyMapper.toProfileTechnologyWithTechnologies(
                                profileTechnology, technologies)));
  }

  private Mono<Profile> registerWithTechnologies(Profile profile) {
    return technologyServiceGateway
        .technologiesExists(profile.technologiesId())
        .filter(Boolean.TRUE::equals)
        .switchIfEmpty(Mono.error(TechnologiesNotFound::new))
        .flatMap(exists -> saveProfileWithRelation(profile));
  }

  private Mono<Profile> saveProfileWithRelation(Profile profile) {
    List<Long> technologiesIds = profile.technologiesId();
    return profilePersistencePort
        .save(profile)
        .flatMap(
            savedProfile ->
                createTechnologyRelation(savedProfile.id(), technologiesIds)
                    .thenReturn(savedProfile));
  }

  private Mono<Void> createTechnologyRelation(Long profileId, List<Long> technologiesIds) {
    return technologyServiceGateway.createRelation(
        new TechnologyProfileDto(
            technologiesIds.stream()
                .map(technologyId -> new TechnologyProfileRelation(technologyId, profileId))
                .toList()));
  }
}
