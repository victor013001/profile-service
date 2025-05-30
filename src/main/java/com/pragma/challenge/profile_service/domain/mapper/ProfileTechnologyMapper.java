package com.pragma.challenge.profile_service.domain.mapper;

import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import com.pragma.challenge.profile_service.domain.model.TechnologyNoDescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileTechnologyMapper {
  @Mapping(target = "technologies", source = "technologies")
  ProfileTechnology toProfileTechnologyWithTechnologies(
      ProfileTechnology profileTechnology, List<TechnologyNoDescription> technologies);
}
