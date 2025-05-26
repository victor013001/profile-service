package com.pragma.challenge.profile_service.infrastructure.entrypoints.dto;

public record DefaultServerResponse<T, E>(T data, E error) {}
