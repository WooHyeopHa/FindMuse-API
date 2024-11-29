package com.whh.findmuseapi.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RefreshTokenDto(
    @JsonProperty(value = "refreshToken") String refreshToken
) {}
