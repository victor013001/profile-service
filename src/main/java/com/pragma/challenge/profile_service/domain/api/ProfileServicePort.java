package com.pragma.challenge.profile_service.domain.api;

import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileServicePort {
  Mono<Profile> registerProfile(Profile profile);

  Flux<ProfileTechnology> getProfiles(PageRequest pageRequest);
}
