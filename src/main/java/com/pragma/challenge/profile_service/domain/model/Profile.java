package com.pragma.challenge.profile_service.domain.model;

import java.util.List;

public record Profile(Long id, String name, String description, List<Long> technologiesId) {}
