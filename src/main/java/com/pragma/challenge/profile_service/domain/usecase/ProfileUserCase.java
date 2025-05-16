package com.pragma.challenge.profile_service.domain.usecase;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProfileUserCase implements ProfileServicePort {

  private final ProfilePersistencePort profilePersistencePort;

  @Override
  public Mono<Profile> registerProfile(Profile profile) {
    return profilePersistencePort
        .validName(profile.name())
        .then(Mono.defer(() -> profilePersistencePort.save(profile)));
  }
}
