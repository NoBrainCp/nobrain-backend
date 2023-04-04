package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.LoginRequest;
import com.nobrain.bookmarking.domain.auth.dto.LoginResponse;
import com.nobrain.bookmarking.domain.auth.dto.TokenDto;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import com.nobrain.bookmarking.domain.auth.repository.RefreshTokenRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserLoginIdNotFoundException;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final static String tokenType = "Bearer";

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public LoginResponse login(LoginRequest dto) {
        User user = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(() -> new UserLoginIdNotFoundException(dto.getLoginId()));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }

        UserPayload userPayload = new UserPayload(user.getId(), user.getName(), user.getRoles());
        TokenDto tokenDto = new TokenDto();
        tokenDto.setTokenType(tokenType);
        tokenDto.setAccessToken(tokenProvider.generateToken(userPayload));

        if (dto.isKeepLoggedIn()) {
            RefreshToken refreshToken = tokenProvider.generateRefreshToken(user.getId());
            tokenDto.setRefreshToken(refreshToken.getRefreshToken());
        }

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .tokenDto(tokenDto)
                .build();
    }

    public void logout(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
