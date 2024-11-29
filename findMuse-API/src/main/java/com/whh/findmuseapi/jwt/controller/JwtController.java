package com.whh.findmuseapi.jwt.controller;

import com.whh.findmuseapi.common.constant.ResponseCode;
import com.whh.findmuseapi.common.util.ApiResponse;
import com.whh.findmuseapi.jwt.dto.RefreshTokenDto;
import com.whh.findmuseapi.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;
    
    @Operation(summary = "Refresh Token 재발급")
    @PatchMapping("/refresh-token")
    public ApiResponse<RefreshTokenDto> reIssueRefreshToken(RefreshTokenDto refreshTokenDto) {
        log.info(refreshTokenDto.toString());
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, jwtService.reIssueRefreshToken(refreshTokenDto));
    }

}
