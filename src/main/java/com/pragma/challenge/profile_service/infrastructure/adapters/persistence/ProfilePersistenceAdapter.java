package com.pragma.challenge.profile_service.infrastructure.adapters.persistence;

import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.ProfileAlreadyExists;
import com.pragma.challenge.profile_service.domain.model.BootcampProfile;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper.BootcampProfileEntityMapper;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper.ProfileEntityMapper;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.BootcampProfileRepository;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.ProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements ProfilePersistencePort {
  private static final String LOG_PREFIX = "[PROFILE_PERSISTENCE_ADAPTER] >>>";

  private final ProfileRepository profileRepository;
  private final ProfileEntityMapper profileEntityMapper;
  private final BootcampProfileRepository bootcampProfileRepository;
  private final BootcampProfileEntityMapper bootcampProfileEntityMapper;

  @Override
  public Mono<Profile> save(Profile profile) {
    log.info(
        "{} Saving profile with name: {} and description: {}.",
        LOG_PREFIX,
        profile.name(),
        profile.description());
    return profileRepository
        .save(profileEntityMapper.toEntity(profile))
        .map(profileEntityMapper::toModel);
  }

  @Override
  public Mono<Void> validName(String name) {
    return profileRepository
        .existsByName(name)
        .flatMap(
            exist -> {
              if (Boolean.TRUE.equals(exist)) {
                log.info("{} Profile name: {} already exist", LOG_PREFIX, name);
                return Mono.error(ProfileAlreadyExists::new);
              }
              return Mono.empty();
            });
  }

  @Override
  public Flux<ProfileTechnology> findAllBy(PageRequest pageRequest) {
    return profileRepository.findAllBy(pageRequest).map(profileEntityMapper::toProfileTechnology);
  }

  @Override
  public Mono<BootcampProfile> saveTechnologyProfile(BootcampProfile bootcampProfile) {
    log.info(
        "{} Saving relation with bootcamp id: {} and profile id: {}.",
        LOG_PREFIX,
        bootcampProfile.bootcampId(),
        bootcampProfile.profileId());
    return bootcampProfileRepository
        .save(bootcampProfileEntityMapper.toEntity(bootcampProfile))
        .map(bootcampProfileEntityMapper::toModel);
  }

  @Override
  public Mono<Boolean> existsById(Long id) {
    log.info("{} Checking if profile with id: {} exists.", LOG_PREFIX, id);
    return profileRepository.existsById(id);
  }

  @Override
  public Flux<ProfileTechnology> findAllByBootcampId(long bootcampId) {
    log.info("{} Finding profiles for bootcamp with id: {}", LOG_PREFIX, bootcampId);
    return profileRepository
        .findAllByBootcampId(bootcampId)
        .map(profileEntityMapper::toProfileTechnology);
  }

  @Override
  public Mono<List<Long>> findProfileIdsByOnlyBootcampId(Long bootcampId) {
    log.info(
        "{} Finding profile ids that have a relation with the bootcamp id: {}",
        LOG_PREFIX,
        bootcampId);
    return bootcampProfileRepository.findProfileIdsByOnlyBootcampId(bootcampId).collectList();
  }

  @Override
  public Mono<Void> deleteByBootcampId(Long bootcampId) {
    log.info("{} Deleting relation where bootcamp id: {}", LOG_PREFIX, bootcampId);
    return bootcampProfileRepository.deleteByBootcampId(bootcampId);
  }

  @Override
  public Mono<Void> deleteProfilesByIds(List<Long> profileIds) {
    log.info("{} Deleting profiles with ids: {}", LOG_PREFIX, profileIds);
    return profileRepository.deleteProfilesByIds(profileIds);
  }
}
