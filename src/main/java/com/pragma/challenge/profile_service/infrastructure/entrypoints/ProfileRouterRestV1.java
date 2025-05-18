package com.pragma.challenge.profile_service.infrastructure.entrypoints;

import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileDto;
import com.pragma.challenge.profile_service.domain.exceptions.StandardError;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.ProfileHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProfileRouterRestV1 {
  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/api/v1/profile",
        method = RequestMethod.POST,
        beanClass = ProfileHandler.class,
        beanMethod = "createProfile",
        operation =
            @Operation(
                operationId = "createProfile",
                summary = "Create new profile",
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ProfileDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = "Profile created successfully.",
                      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                  @ApiResponse(
                      responseCode = "400",
                      description = "Unable to process the request with the given data.",
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "409",
                      description = "Conflict with the given data.",
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = "An unexpected error occurred on the server.",
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                }))
  })
  public RouterFunction<ServerResponse> routerFunction(ProfileHandler profileHandler) {
    return nest(
        path("/api/v1/profile"), route(RequestPredicates.POST(""), profileHandler::createProfile));
  }
}
