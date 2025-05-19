package com.pragma.challenge.profile_service.domain.spi;

import com.pragma.challenge.profile_service.domain.model.Profile;
import reactor.core.publisher.Mono;

public interface ProfilePersistencePort {
  Mono<Profile> save(Profile profile);

  Mono<Void> validName(String name);
}
