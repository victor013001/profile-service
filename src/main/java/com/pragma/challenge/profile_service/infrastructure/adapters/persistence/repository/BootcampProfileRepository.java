package com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.BootcampProfileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BootcampProfileRepository
    extends ReactiveCrudRepository<BootcampProfileEntity, Long> {}
