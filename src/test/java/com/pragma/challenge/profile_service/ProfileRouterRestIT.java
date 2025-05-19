package com.pragma.challenge.profile_service;

import static com.pragma.challenge.profile_service.util.ProfileDtoData.getInvalidProfileDto;
import static com.pragma.challenge.profile_service.util.ProfileDtoData.getProfileDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.pragma.challenge.profile_service.domain.enums.ServerResponses;
import com.pragma.challenge.profile_service.domain.exceptions.StandardError;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity.ProfileEntity;
import com.pragma.challenge.profile_service.infrastructure.adapters.persistence.repository.ProfileRepository;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import java.util.List;

import com.pragma.challenge.profile_service.infrastructure.entrypoints.util.Constants;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileRouterRestIT {

  private final String BASE_PATH = "/api/v1/profile";
  private final String TECHNOLOGY_SERVICE_PATH = "/api/v1/technology";

  @Autowired WebTestClient webTestClient;
  @Autowired ProfileRepository profileRepository;

  @BeforeEach
  void setUp() {
    profileRepository
        .saveAll(
            List.of(
                ProfileEntity.builder()
                    .name("QA")
                    .description("Ensures software products meet quality standards before release")
                    .build()))
        .blockLast();
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
        .expectBody(StandardError.class)
        .consumeWith(
            exchangeResult -> {
              var error = exchangeResult.getResponseBody();
              assertNotNull(error);
              assertEquals(ServerResponses.BAD_REQUEST.getMessage(), error.getDescription());
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
        .expectBody(StandardError.class)
        .consumeWith(
            exchangeResult -> {
              var error = exchangeResult.getResponseBody();
              assertNotNull(error);
              assertEquals(
                  ServerResponses.TECHNOLOGIES_NOT_FOUND.getMessage(), error.getDescription());
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
              var error = exchangeResult.getResponseBody();
              assertNotNull(error);
              System.out.println(error);
            });
  }
}
