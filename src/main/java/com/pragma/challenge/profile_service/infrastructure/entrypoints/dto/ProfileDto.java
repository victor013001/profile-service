package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ProfileDto(
    @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name exceeds the permitted limit")
        String name,
    @NotBlank(message = "Description is mandatory")
        @Size(max = 90, message = "Name exceeds the permitted limit")
        String description,
    @NotNull(message = "Technologies are mandatory")
        @Size(
            max = 20,
            min = 3,
            message = "A minimum of 3 and a maximum of 20 technologies must be specified.")
        Set<Long> technologiesId) {}
