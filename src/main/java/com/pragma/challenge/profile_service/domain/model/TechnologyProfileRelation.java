package com.pragma.challenge.profile_service.domain.model;

import com.pragma.challenge.profile_service.domain.validation.annotation.NotNull;

public record TechnologyProfileRelation(@NotNull Long technologyId, @NotNull Long profileId) {}
