package com.pragma.challenge.profile_service;

import static com.pragma.challenge.profile_service.util.ProfileDtoData.getInvalidProfileDto;
import static com.pragma.challenge.profile_service.util.ProfileDtoData.getProfileDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.pragma.challenge.profile_service.domain.constants.Constants;
import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import com.pragma.challenge.profile_service.domain.exceptions.StandardError;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.BootcampProfileEntity;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.ProfileEntity;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.BootcampProfileRepository;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.ProfileRepository;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileRouterRestIT {

  private final String BASE_PATH = "/api/v1/profile";
  private final String TECHNOLOGY_SERVICE_PATH = "/api/v1/technology";

  @Autowired WebTestClient webTestClient;
  @Autowired ProfileRepository profileRepository;
  @Autowired BootcampProfileRepository bootcampProfileRepository;

  @BeforeEach
  void setUp() {
    List<ProfileEntity> savedProfiles =
        profileRepository
            .saveAll(
                List.of(
                    ProfileEntity.builder()
                        .name("QA")
                        .description(
                            "Ensures software products meet quality standards before release")
                        .build(),
                    ProfileEntity.builder()
                        .name("DevOps")
                        .description(
                            "Bridges the gap between development and operations to streamline software delivery")
                        .build(),
                    ProfileEntity.builder()
                        .name("Data Scientist")
                        .description(
                            "Analyzes complex data to support decision-making and build predictive models")
                        .build()))
            .collectList()
            .block();

    bootcampProfileRepository
        .saveAll(
            List.of(
                BootcampProfileEntity.builder()
                    .profileId(savedProfiles.get(0).getId())
                    .bootcampId(1L)
                    .build(),
                BootcampProfileEntity.builder()
                    .profileId(savedProfiles.get(1).getId())
                    .bootcampId(1L)
                    .build(),
                BootcampProfileEntity.builder()
                    .profileId(savedProfiles.get(1).getId())
                    .bootcampId(2L)
                    .build()))
        .blockLast();
  }

  @AfterEach
  void tearDown() {
    bootcampProfileRepository.deleteAll().block();
    profileRepository.deleteAll().block();
  }

  @Test
  void createProfile() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlPathEqualTo(TECHNOLOGY_SERVICE_PATH + "/exists"))
            .withQueryParam("id", WireMock.matching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("technologiesExists.json")));

    WireMock.stubFor(
        WireMock.post(WireMock.urlEqualTo(TECHNOLOGY_SERVICE_PATH + "/profile"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("technologyProfileRelationSuccess.json")));

    webTestClient
        .post()
        .uri(BASE_PATH)
        .bodyValue(getProfileDto())
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(DefaultServerResponse.class)
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertEquals(ServerResponses.PROFILE_CREATED.getMessage(), response.data());
            });
  }

  @Test
  void createProfileBadRequest() {
    webTestClient
        .post()
        .uri(BASE_PATH)
        .bodyValue(getInvalidProfileDto())
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Object, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertEquals(
                  ServerResponses.BAD_REQUEST.getMessage(), response.error().getDescription());
            });
  }

  @Test
  void createProfileBadTechnologies() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlPathEqualTo(TECHNOLOGY_SERVICE_PATH + "/exists"))
            .withQueryParam("id", WireMock.matching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("technologiesNotExists.json")));

    webTestClient
        .post()
        .uri(BASE_PATH)
        .bodyValue(getProfileDto())
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Object, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertEquals(
                  ServerResponses.TECHNOLOGIES_NOT_FOUND.getMessage(),
                  response.error().getDescription());
            });
  }

  @Test
  void getProfiles() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlPathEqualTo(TECHNOLOGY_SERVICE_PATH))
            .withQueryParam(Constants.PROFILE_ID_PARAM, WireMock.matching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("findTechnologiesByProfileId.json")));

    webTestClient
        .get()
        .uri(BASE_PATH)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(DefaultServerResponse.class)
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
            });
  }

  @Test
  void profilesExists() {
    webTestClient
        .get()
        .uri(BASE_PATH + "/exists?id=1&id=2&id=3")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Boolean, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertTrue(response.data());
            });
  }

  @Test
  void deleteProfile() {
    WireMock.stubFor(
        WireMock.delete(WireMock.urlPathMatching(TECHNOLOGY_SERVICE_PATH))
            .withQueryParam(Constants.PROFILE_ID_PARAM, WireMock.matching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("deletedTechnologies.json")));

    webTestClient
        .delete()
        .uri(BASE_PATH + "/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(DefaultServerResponse.class)
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
            });
  }
}
