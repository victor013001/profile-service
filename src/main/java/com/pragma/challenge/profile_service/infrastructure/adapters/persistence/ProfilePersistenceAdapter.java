package com.pragma.challenge.profile_service.infrastructure.adapters.persistence;

import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper.ProfileEntityMapper;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.ProfileRepository;
import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.ProfileAlreadyExists;
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
}
