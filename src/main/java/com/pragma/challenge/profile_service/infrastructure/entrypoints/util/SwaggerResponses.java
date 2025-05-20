package com.pragma.challenge.profile_service.infrastructure.entrypoints.util;

import com.pragma.challenge.profile_service.domain.model.ProfileTechnology;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class SwaggerResponses {

  private SwaggerResponses() throws InstantiationException {
    throw new InstantiationException("Utility class");
  }

  @Data
  @Schema(name = "DefaultMessageResponse")
  @AllArgsConstructor
  public static class DefaultMessageResponse {
    private String data;
  }

  @Data
  @Schema(name = "DefaultProfileTechnologyResponse")
  @AllArgsConstructor
  public static class DefaultProfileTechnologyResponse {
    private ProfileTechnology data;
  }

  @Data
  @Schema(name = "DefaultBooleanResponse")
  @AllArgsConstructor
  public static class DefaultBooleanResponse {
    private Boolean data;
  }
}
