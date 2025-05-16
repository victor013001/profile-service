package com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileEntityMapper {
  @Mapping(target = "technologiesId", ignore = true)
  Profile toModel(ProfileEntity entity);

  ProfileEntity toEntity(Profile profile);
}
