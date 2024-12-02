package com.whh.findmuseapi.jwt.controller;

import com.whh.findmuseapi.common.constant.ResponseCode;
import com.whh.findmuseapi.common.exception.CBadRequestException;
import com.whh.findmuseapi.common.util.ApiResponse;
import com.whh.findmuseapi.jwt.dto.AccessTokenDto;
import com.whh.findmuseapi.jwt.dto.RefreshTokenDto;
import com.whh.findmuseapi.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, jwtService.reIssueRefreshToken(refreshTokenDto));
    }
    
    @Operation(summary = "Access Token 재발급")
    @PatchMapping("/access-token")
    public ApiResponse<AccessTokenDto> reIssueAccessToken(RefreshTokenDto refreshTokenDto) {
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, jwtService.reIssueAccessToken(refreshTokenDto));
    }
    
    /**
     * Test API
     */
    @Operation(summary = "테스트 유저의 만료된 AccessToken 발급")
    @PostMapping("/issue/expire-access")
    public ApiResponse<String> issueExpiredAccessToken() {
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, jwtService.issueExpiredAccessToken());
    }
    
    @Operation(summary = "테스트 유저의 만료된 RefreshToken 발급 후 저장")
    @GetMapping("/issue/expire-refresh")
    public ApiResponse<String> issueExpiredRefreshToken() {
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, jwtService.issueExpiredRefreshToken());
    }
}
