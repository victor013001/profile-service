package com.pragma.challenge.profile_service.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_bootcamp")
public class BootcampProfileEntity {
  @Id private Long id;
  private Long bootcampId;
  private Long profileId;
}
