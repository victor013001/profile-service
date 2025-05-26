package com.pragma.challenge.profile_service.domain.model;

import com.pragma.challenge.profile_service.domain.validation.annotation.NotNull;

public record BootcampProfile(@NotNull Long bootcampId, @NotNull Long profileId) {}
