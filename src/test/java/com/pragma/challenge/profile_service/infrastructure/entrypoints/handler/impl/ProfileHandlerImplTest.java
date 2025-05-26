package com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.impl;

import static com.pragma.challenge.profile_service.util.ProfileDtoData.getProfileDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pragma.challenge.profile_service.domain.api.ProfileServicePort;
import com.pragma.challenge.profile_service.domain.model.Profile;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.DefaultServerResponseMapper;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.DefaultServerResponseMapperImpl;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.ProfileMapper;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.mapper.ProfileMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProfileHandlerImplTest {
  @InjectMocks ProfileHandlerImpl profileHandler;

  @Mock ProfileServicePort profileServicePort;

  @Spy ProfileMapper profileMapper = new ProfileMapperImpl();

  @Spy
  DefaultServerResponseMapper defaultServerResponseMapper = new DefaultServerResponseMapperImpl();

  @Test
  void shouldCreateProfile() {
    var profileDto = getProfileDto();
    var request = MockServerRequest.builder().body(Mono.just(profileDto));
    when(profileServicePort.registerProfile(any(Profile.class)))
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

    StepVerifier.create(profileHandler.createProfile(request))
        .assertNext(
            serverResponse -> {
              assert serverResponse.statusCode().isSameCodeAs(HttpStatus.CREATED);
            })
        .verifyComplete();
  }
}
