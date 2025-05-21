package com.pragma.challenge.profile_service.infrastructure.entrypoints;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.pragma.challenge.profile_service.domain.constants.Constants;
import com.pragma.challenge.profile_service.domain.exceptions.StandardError;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.BootcampProfiles;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.dto.ProfileDto;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.handler.ProfileHandler;
import com.pragma.challenge.profile_service.infrastructure.entrypoints.util.SwaggerResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
                      description = Constants.PROFILE_CREATED_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultMessageResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = Constants.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "404",
                      description = Constants.TECHNOLOGIES_NOT_FOUND_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "409",
                      description = Constants.PROFILE_ALREADY_EXISTS_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = Constants.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                })),
    @RouterOperation(
        path = "/api/v1/profile",
        method = RequestMethod.GET,
        beanClass = ProfileHandler.class,
        beanMethod = "getProfiles",
        operation =
            @Operation(
                operationId = "getProfiles",
                summary = "Get profiles ordered by name or technologies size",
                parameters = {
                  @Parameter(
                      in = ParameterIn.QUERY,
                      name = Constants.SORT_DIRECTION,
                      description = "ASC for ascending or DESC for descending order."),
                  @Parameter(
                      in = ParameterIn.QUERY,
                      name = Constants.SORT_BY,
                      description = "Field to sort by. Accepted values: 'name' or 'technologies'.")
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Profiles with technologies.",
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultProfileTechnologyResponse
                                              .class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = Constants.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                })),
    @RouterOperation(
        path = "/api/v1/profile/exists",
        method = RequestMethod.GET,
        beanClass = ProfileHandler.class,
        beanMethod = "profileExists",
        operation =
            @Operation(
                operationId = "profilesExists",
                summary = "Check if profiles exist",
                parameters = {
                  @Parameter(
                      in = ParameterIn.QUERY,
                      name = "id",
                      description = "Profile IDs to check existence",
                      required = true,
                      array = @ArraySchema(schema = @Schema(type = "string", example = "1")))
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Existence check completed.",
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultBooleanResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = Constants.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = Constants.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                })),
    @RouterOperation(
        path = "/api/v1/profile/bootcamp",
        method = RequestMethod.POST,
        beanClass = ProfileHandler.class,
        beanMethod = "createRelation",
        operation =
            @Operation(
                operationId = "createRelation",
                summary = "Create profile and technology relation.",
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = BootcampProfiles.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = Constants.BOOTCAMP_PROFILE_CREATED_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultMessageResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = Constants.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = Constants.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                })),
    @RouterOperation(
        path = "/api/v1/profile/{id}",
        method = RequestMethod.DELETE,
        beanClass = ProfileHandler.class,
        beanMethod = "deleteProfileForBootcamp",
        operation =
            @Operation(
                operationId = "deleteProfileForBootcamp",
                summary = "Delete a profile by bootcamp id",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "id",
                      required = true,
                      description = "Id of the bootcamp to delete the profiles")
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = Constants.PROFILES_DELETED_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultMessageResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = Constants.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = Constants.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                })),
    @RouterOperation(
        path = "/api/v1/technology",
        method = RequestMethod.DELETE,
        beanClass = ProfileHandler.class,
        beanMethod = "deleteProfile",
        operation =
            @Operation(
                operationId = "deleteProfile",
                summary = "Delete a profile by ID",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "id",
                      required = true,
                      description = "ID of the profile to delete")
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = Constants.PROFILES_DELETED_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultMessageResponse.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = Constants.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema = @Schema(implementation = StandardError.class)))
                }))
  })
  public RouterFunction<ServerResponse> routerFunction(ProfileHandler profileHandler) {
    return nest(
        path("/api/v1/profile"),
        route(RequestPredicates.POST(""), profileHandler::createProfile)
            .andRoute(RequestPredicates.GET(""), profileHandler::getProfiles)
            .andRoute(RequestPredicates.GET("/exists"), profileHandler::profileExists)
            .andRoute(RequestPredicates.POST("/bootcamp"), profileHandler::createRelation)
            .andRoute(RequestPredicates.GET(""), profileHandler::getProfilesByBootcampId)
            .andRoute(RequestPredicates.DELETE("/{id}"), profileHandler::deleteProfile));
  }
}
