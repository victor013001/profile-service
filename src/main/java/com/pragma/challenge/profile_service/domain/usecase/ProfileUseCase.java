package com.pragma.challenge.profile_service.domain.usecase;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.TechnologiesNotFound;
import com.pragma.challenge.profile_service.domain.mapper.ProfileTechnologyMapper;
import com.pragma.challenge.profile_service.domain.model.BootcampProfile;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileIds;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileDto;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileRelation;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import com.pragma.challenge.profile_service.domain.spi.TechnologyServiceGateway;
import com.pragma.challenge.profile_service.domain.validation.ValidListAnnotation;
import com.pragma.challenge.profile_service.domain.validation.ValidNotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ProfileUseCase implements ProfileServicePort {
  private static final String LOG_PREFIX = "[PROFILE_USE_CASE] >>>";

  private final ProfilePersistencePort profilePersistencePort;
  private final TechnologyServiceGateway technologyServiceGateway;
  private final ProfileTechnologyMapper profileTechnologyMapper;

  @Override
  public Mono<Profile> registerProfile(Profile profile) {
    return Mono.just(profile)
        .flatMap(
            profile1 -> {
              ValidListAnnotation.valid(profile1);
              ValidNotNull.valid(profile1);
              return Mono.just(profile1);
            })
        .flatMap(
            profile1 ->
                profilePersistencePort
                    .validName(profile1.name())
                    .then(Mono.defer(() -> registerWithTechnologies(profile1))));
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

  @Override
  public Mono<Void> registerBootcampProfileRelation(List<BootcampProfile> bootcampProfile) {
    return Flux.fromIterable(bootcampProfile)
        .flatMap(profilePersistencePort::saveTechnologyProfile)
        .then();
  }

  @Override
  public Mono<Boolean> checkProfileIds(ProfileIds profileIds) {
    return Flux.fromIterable(profileIds.ids())
        .flatMap(
            id ->
                profilePersistencePort
                    .existsById(id)
                    .flatMap(
                        exists -> {
                          if (Boolean.FALSE.equals(exists)) {
                            log.error("{} Profile with id: {} was not found", LOG_PREFIX, id);
                          }
                          return Mono.just(exists);
                        }))
        .any(result -> !result)
        .flatMap(foundFalse -> Mono.just(!foundFalse));
  }

  @Override
  public Mono<List<ProfileTechnology>> getBootcampProfiles(long bootcampId) {
    return profilePersistencePort
        .findAllByBootcampId(bootcampId)
        .flatMap(
            profileTechnology ->
                technologyServiceGateway
                    .getTechnologies(profileTechnology.id())
                    .map(
                        technologies ->
                            profileTechnologyMapper.toProfileTechnologyWithTechnologies(
                                profileTechnology, technologies)))
        .collectList();
  }

  @Override
  public Mono<Void> delete(Long bootcampId) {
    return profilePersistencePort
        .findProfileIdsByOnlyBootcampId(bootcampId)
        .flatMap(
            profileIds ->
                technologyServiceGateway
                    .deleteProfileTechnologies(profileIds)
                    .then(profilePersistencePort.deleteByBootcampId(bootcampId))
                    .then(profilePersistencePort.deleteProfilesByIds(profileIds)));
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
