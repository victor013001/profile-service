package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotNull;

public record BootcampProfileRelation(
    @NotNull(message = "The bootcamp id is mandatory.") Long bootcampId,
    @NotNull(message = "The profile id is mandatory.") Long profileId) {}
