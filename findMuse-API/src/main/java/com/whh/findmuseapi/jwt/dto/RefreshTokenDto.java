package com.whh.findmuseapi.jwt.dto;


import lombok.Builder;

@Builder
public record RefreshTokenDto(
    String refreshToken
) {}
