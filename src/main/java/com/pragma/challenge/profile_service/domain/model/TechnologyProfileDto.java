package com.pragma.challenge.profile_service.domain.model;

import com.pragma.challenge.profile_service.domain.validation.annotation.ValidList;
import java.util.List;

public record TechnologyProfileDto(@ValidList(min = 1) List<TechnologyProfileRelation> relations) {}
