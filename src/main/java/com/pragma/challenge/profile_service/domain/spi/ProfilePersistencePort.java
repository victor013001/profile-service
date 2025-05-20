package com.pragma.challenge.profile_service.domain.spi;

import com.pragma.challenge.profile_service.domain.model.BootcampProfile;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfilePersistencePort {
  Mono<Profile> save(Profile profile);

  Mono<Void> validName(String name);

  Flux<ProfileTechnology> findAllBy(PageRequest pageRequest);

  Mono<BootcampProfile> saveTechnologyProfile(BootcampProfile bootcampProfile);

  Mono<Boolean> existsById(Long id);

  Flux<ProfileTechnology> findAllByBootcampId(long bootcampId);
}
