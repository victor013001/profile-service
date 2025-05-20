package com.pragma.challenge.profile_service.domain.api;

import com.pragma.challenge.profile_service.domain.model.BootcampProfile;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.model.ProfileIds;
import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileServicePort {
  Mono<Profile> registerProfile(Profile profile);

  Flux<ProfileTechnology> getProfiles(PageRequest pageRequest);

  Mono<Void> registerBootcampProfileRelation(List<BootcampProfile> bootcampProfile);

  Mono<Boolean> checkProfileIds(ProfileIds profileIds);

  Mono<List<ProfileTechnology>> getBootcampProfiles(long bootcampId);

  Mono<Void> delete(Long bootcampId);
}
