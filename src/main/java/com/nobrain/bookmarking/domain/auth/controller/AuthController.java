package com.nobrain.bookmarking.domain.auth.controller;

import com.nobrain.bookmarking.domain.auth.dto.AccessTokenRequest;
import com.nobrain.bookmarking.domain.auth.dto.RefreshTokenRequest;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final ResponseService responseService;

    @PostMapping("/refresh-token")
    public SingleResult<RefreshToken> generateRefreshToken(@RequestBody RefreshTokenRequest request) {
        return responseService.getSingleResult(tokenService.generateRefreshToken(request));
    }

    @PostMapping("/access-token")
    public SingleResult<String> generateAccessToken(@RequestBody AccessTokenRequest request) {
        return responseService.getSingleResult(tokenService.generateAccessToken(request));
    }
}
