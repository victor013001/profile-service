package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;


import java.util.List;

public record BootcampProfiles(List<BootcampProfileRelation> relations) {}
