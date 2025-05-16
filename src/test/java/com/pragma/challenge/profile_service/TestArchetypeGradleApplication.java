package com.pragma.challenge.profile_service;

import org.springframework.boot.SpringApplication;

public class TestArchetypeGradleApplication {
  public static void main(String[] args) {
    SpringApplication.from(ProfileServiceApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
