package com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper;

import com.pragma.challenge.profile_service.domain.model.BootcampProfile;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.BootcampProfileRelation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BootcampProfileMapper {
  List<BootcampProfile> toBootcampProfile(
      List<BootcampProfileRelation> bootcampProfileRelations);
}
