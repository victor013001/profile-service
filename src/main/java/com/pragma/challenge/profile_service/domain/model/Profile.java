package com.pragma.challenge.profile_service.domain.model;

import com.pragma.challenge.profile_service.domain.validation.annotation.NotNull;
import com.pragma.challenge.profile_service.domain.validation.annotation.ValidList;
import java.util.List;

public record Profile(
    Long id,
    @NotNull String name,
    @NotNull String description,
    @ValidList(min = 3) List<Long> technologiesId) {}
