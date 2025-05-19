package com.pragma.challenge.profile_service.domain.spi;

import com.pragma.challenge.profile_service.domain.model.TechnologyNoDescription;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyServiceGateway {
  Mono<Boolean> technologiesExists(List<Long> technologiesId);

  Mono<Void> createRelation(TechnologyProfileDto technologyProfileDto);

  Mono<List<TechnologyNoDescription>> getTechnologies(Long profileId);
}
