package com.pragma.challenge.profile_service.util;

import com.pragma.challenge.profile_service.domain.model.Profile;

import java.util.List;

public class ProfileData {
  private ProfileData() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static Profile getProfileWithoutId() {
    return new Profile(null, "Backend", "Backend developer with React and AWS", List.of(1L, 2L, 3L));
  }
}
