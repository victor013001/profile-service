package com.pragma.challenge.profile_service.domain.api;

import com.pragma.challenge.profile_service.domain.model.Profile;
import reactor.core.publisher.Mono;

public interface ProfileServicePort {
  Mono<Profile> registerProfile(Profile profile);
}
