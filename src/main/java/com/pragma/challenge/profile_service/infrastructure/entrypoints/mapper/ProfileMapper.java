package com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper;

import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
  @Mapping(target = "id", ignore = true)
  Profile toProfile(ProfileDto profileDto);
}
