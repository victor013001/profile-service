package com.pragma.challenge.profile_service.application.config;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.domain.mapper.ProfileTechnologyMapper;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import com.pragma.challenge.profile_service.domain.spi.TechnologyServiceGateway;
import com.pragma.challenge.profile_service.domain.usecase.ProfileUseCase;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.ProfilePersistenceAdapter;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper.BootcampProfileEntityMapper;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.mapper.ProfileEntityMapper;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.BootcampProfileRepository;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

  private final ProfileRepository profileRepository;
  private final ProfileEntityMapper profileEntityMapper;
  private final BootcampProfileRepository bootcampProfileRepository;
  private final BootcampProfileEntityMapper bootcampProfileEntityMapper;

  @Bean
  public ProfileServicePort profileServicePort(
      ProfilePersistencePort profilePersistencePort,
      TechnologyServiceGateway technologyServiceGateway,
      ProfileTechnologyMapper profileTechnologyMapper) {
    return new ProfileUseCase(
        profilePersistencePort, technologyServiceGateway, profileTechnologyMapper);
  }

  @Bean
  public ProfilePersistencePort profilePersistencePort() {
    return new ProfilePersistenceAdapter(
        profileRepository,
        profileEntityMapper,
        bootcampProfileRepository,
        bootcampProfileEntityMapper);
  }
}
