package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotNull;

public record TechnologyProfileRelationDto(
    @NotNull(message = "The technology id is mandatory.") Long technologyId,
    @NotNull(message = "The profile id is mandatory.") Long profileId) {}
