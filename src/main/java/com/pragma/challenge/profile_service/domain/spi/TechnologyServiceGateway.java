package com.pragma.challenge.profile_service.domain.spi;

import com.pragma.challenge.profile_service.domain.model.TechnologyNoDescription;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileDto;
import java.util.List;
import reactor.core.publisher.Mono;

public interface TechnologyServiceGateway {
  Mono<Boolean> technologiesExists(List<Long> technologiesId);

  Mono<Void> createRelation(TechnologyProfileDto technologyProfileDto);

  Mono<List<TechnologyNoDescription>> getTechnologies(Long profileId);

  Mono<Void> deleteProfileTechnologies(List<Long> profileIds);
}
