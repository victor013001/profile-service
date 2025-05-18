package com.pragma.challenge.profile_service.domain.usecase;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileUserCase implements ProfileServicePort {
  private static final String LOG_PREFIX = "[PROFILE_USE_CASE] >>>";

  private final ProfilePersistencePort profilePersistencePort;

  @Override
  @Transactional
  public Mono<Profile> registerProfile(Profile profile) {
    return profilePersistencePort
        .validName(profile.name())
        .then(Mono.defer(() -> profilePersistencePort.save(profile)));
  }
}
