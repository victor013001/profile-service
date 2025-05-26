package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;


import java.util.Set;

public record ProfileDto(String name, String description, Set<Long> technologiesId) {}
