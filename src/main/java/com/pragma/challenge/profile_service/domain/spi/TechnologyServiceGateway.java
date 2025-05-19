package com.pragma.challenge.profile_service.domain.spi;

import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import com.pragma.challenge.profile_service.domain.model.Technology;
import com.pragma.challenge.profile_service.infrastructure.adapters.technology_service.dto.TechnologyNoDescription;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.TechnologyProfileDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyServiceGateway {
  Mono<Boolean> technologiesExists(List<Long> technologiesId);

  Mono<Void> createRelation(TechnologyProfileDto technologyProfileDto);

  Mono<List<TechnologyNoDescription>> getTechnologies(Long profileId);
}

