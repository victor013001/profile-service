package com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.ProfileEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<ProfileEntity, Long> {
  Mono<Boolean> existsByName(String name);
  Flux<ProfileEntity> findAllBy(Pageable pageable);
}
