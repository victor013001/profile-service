package com.pragma.challenge.profile_service.domain.model;

import jakarta.validation.constraints.NotNull;

public record TechnologyProfileRelation(
    @NotNull(message = "The technology id is mandatory.") Long technologyId,
    @NotNull(message = "The profile id is mandatory.") Long profileId) {}
