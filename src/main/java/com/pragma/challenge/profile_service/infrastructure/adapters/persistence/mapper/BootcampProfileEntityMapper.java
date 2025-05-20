package com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.profile_service.domain.model.BootcampProfile;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.BootcampProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BootcampProfileEntityMapper {
  BootcampProfile toModel(BootcampProfileEntity entity);

  BootcampProfileEntity toEntity(BootcampProfile bootcampProfile);
}
