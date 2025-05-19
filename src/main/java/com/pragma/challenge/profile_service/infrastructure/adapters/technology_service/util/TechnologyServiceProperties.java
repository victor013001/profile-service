package com.pragma.challenge.profile_service.infrastructure.adapters.technology_service.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("technology-service")
public class TechnologyServiceProperties {
  private String baseUrl;
  private String timeout;
}
