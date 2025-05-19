package com.pragma.challenge.profile_service.domain.spi;

import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.TechnologyProfileDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyServiceGateway {
  Mono<Boolean> technologiesExists(List<Long> technologiesId);

  Mono<Void> createRelation(TechnologyProfileDto technologyProfileDto);
}

