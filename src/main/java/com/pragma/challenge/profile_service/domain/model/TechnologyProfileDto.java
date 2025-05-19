package com.pragma.challenge.profile_service.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record TechnologyProfileDto(
    @Valid
        @NotNull(message = "The relations list is mandatory.")
        @Size(min = 1, message = "The relations list must contain at least one element.")
        List<TechnologyProfileRelation> relations) {}
