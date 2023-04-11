package com.nobrain.bookmarking.domain.auth.controller;

import com.nobrain.bookmarking.domain.auth.dto.AccessTokenRequest;
import com.nobrain.bookmarking.domain.auth.dto.LoginRequest;
import com.nobrain.bookmarking.domain.auth.dto.LoginResponse;
import com.nobrain.bookmarking.domain.auth.dto.LogoutRequest;
import com.nobrain.bookmarking.domain.auth.exception.TokenNotExistsException;
import com.nobrain.bookmarking.domain.auth.service.AuthService;
import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/auth")
public class AuthController {

    private final ResponseService responseService;
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public SingleResult<LoginResponse> login(@RequestBody LoginRequest dto) {
        return responseService.getSingleResult(authService.login(dto));
    }

    @PostMapping("/logout")
    public CommonResult logout(@RequestBody LogoutRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();
        validateRefreshTokenExists(refreshToken);
        authService.logout(refreshToken);
        return responseService.getSuccessResult();
    }

    @PostMapping("/access-token")
    public SingleResult<String> generateAccessToken(@RequestBody AccessTokenRequest accessTokenRequest) {
        validateRefreshTokenExists(accessTokenRequest.getRefreshToken());
        String accessToken = tokenProvider.generateAccessToken(accessTokenRequest.getRefreshToken());
        return responseService.getSingleResult(accessToken);
    }

    private void validateRefreshTokenExists(String refreshToken) {
        if (refreshToken == null) {
            throw new TokenNotExistsException("Refresh Token");
        }
    }
}
