package com.pragma.challenge.profile_service.domain.usecase;

import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.ProfileAlreadyExists;
import com.pragma.challenge.profile_service.domain.exceptions.standard_exception.TechnologiesNotFound;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.domain.spi.ProfilePersistencePort;
import com.pragma.challenge.profile_service.domain.spi.TechnologyServiceGateway;
import com.pragma.challenge.profile_service.domain.model.TechnologyProfileDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.pragma.challenge.profile_service.util.ProfileData.getProfileWithoutId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileUseCaseTest {
  @InjectMocks
  ProfileUseCase profileUseCase;

  @Mock ProfilePersistencePort profilePersistencePort;
  @Mock TechnologyServiceGateway technologyServiceGateway;
  @Mock TransactionalOperator transactionalOperator;

  @Test
  void shouldRegisterProfile() {
    var profile = getProfileWithoutId();
    when(transactionalOperator.transactional(any(Mono.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(profilePersistencePort.validName(profile.name())).thenReturn(Mono.empty());
    when(technologyServiceGateway.technologiesExists(anyList())).thenReturn(Mono.just(true));
    when(profilePersistencePort.save(profile))
        .thenAnswer(
            invocationOnMock -> {
              Profile profileSaved = invocationOnMock.getArgument(0);
              return Mono.just(
                  new Profile(
                      1L,
                      profileSaved.name(),
                      profileSaved.description(),
                      profileSaved.technologiesId()));
            });
    when(technologyServiceGateway.createRelation(any(TechnologyProfileDto.class)))
        .thenReturn(Mono.empty());

    StepVerifier.create(profileUseCase.registerProfile(profile))
        .assertNext(
            savedProfile -> {
              assert Objects.nonNull(savedProfile.id());
            })
        .verifyComplete();
  }

  @Test
  void shouldReturnProfileAlreadyExists() {
    var profile = getProfileWithoutId();
    when(transactionalOperator.transactional(any(Mono.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(profilePersistencePort.validName(profile.name()))
        .thenReturn(Mono.error(ProfileAlreadyExists::new));

    StepVerifier.create(profileUseCase.registerProfile(profile))
        .expectError(ProfileAlreadyExists.class)
        .verify();

    verify(technologyServiceGateway, never()).technologiesExists(anyList());
    verify(technologyServiceGateway, never()).createRelation(any(TechnologyProfileDto.class));
  }

  @Test
  void shouldReturnTechnologiesNotFound() {
    var profile = getProfileWithoutId();
    when(transactionalOperator.transactional(any(Mono.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(profilePersistencePort.validName(profile.name())).thenReturn(Mono.empty());
    when(technologyServiceGateway.technologiesExists(anyList())).thenReturn(Mono.just(false));

    StepVerifier.create(profileUseCase.registerProfile(profile))
        .expectError(TechnologiesNotFound.class)
        .verify();

    verify(technologyServiceGateway, never()).createRelation(any(TechnologyProfileDto.class));
  }
}
