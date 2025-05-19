package com.pragma.challenge.profile_service.domain.model;

import com.pragma.challenge.profile_service.infrastructure.adapters.technology_service.dto.TechnologyNoDescription;

import java.util.List;

public record ProfileTechnology(
    Long id, String name, String description, List<TechnologyNoDescription> technologies) {}
