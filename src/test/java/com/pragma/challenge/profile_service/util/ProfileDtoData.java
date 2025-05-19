package com.pragma.challenge.profile_service.util;

import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileDto;

import java.util.Set;

public class ProfileDtoData {
  private ProfileDtoData() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static ProfileDto getProfileDto() {
    return new ProfileDto("Backend", "Backend developer with React and AWS", Set.of(1L, 2L, 3L));
  }

  public static ProfileDto getInvalidProfileDto() {
    return new ProfileDto("Backend", "Backend developer with React and AWS", Set.of(1L));
  }
}
