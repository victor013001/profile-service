package com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.BootcampProfileEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BootcampProfileRepository
    extends ReactiveCrudRepository<BootcampProfileEntity, Long> {
  @Query(
      "SELECT profile_id FROM profile_bootcamp GROUP BY profile_id HAVING COUNT(*) = 1 AND MAX(bootcamp_id) = :bootcampId")
  Flux<Long> findProfileIdsByOnlyBootcampId(Long bootcampId);

  @Query("DELETE FROM profile_bootcamp WHERE bootcamp_id = :bootcampId")
  Mono<Void> deleteByBootcampId(Long bootcampId);
}
