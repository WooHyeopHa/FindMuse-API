package com.whh.findmuseapi.jwt.controller;

import com.whh.findmuseapi.common.constant.ResponseCode;
import com.whh.findmuseapi.common.util.ApiResponse;
import com.whh.findmuseapi.jwt.dto.RefreshTokenDto;
import com.whh.findmuseapi.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;

    @PatchMapping("/refresh")
    public ApiResponse<RefreshTokenDto> reIssueRefreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, jwtService.reIssueRefreshToken(refreshTokenDto));
    }

}
