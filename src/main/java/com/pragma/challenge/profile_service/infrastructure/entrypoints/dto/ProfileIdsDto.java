package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotNull;;
import java.util.List;

public record ProfileIdsDto(
    @NotNull(message = "The id list is mandatory.")
        List<Long> ids) {}
