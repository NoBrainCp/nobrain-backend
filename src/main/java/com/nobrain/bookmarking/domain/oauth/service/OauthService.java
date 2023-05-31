package com.nobrain.bookmarking.domain.oauth.service;

import com.nobrain.bookmarking.domain.auth.dto.LoginResponse;
import com.nobrain.bookmarking.domain.auth.dto.TokenDto;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import com.nobrain.bookmarking.domain.oauth.OauthAttributes;
import com.nobrain.bookmarking.domain.oauth.OauthProvider;
import com.nobrain.bookmarking.domain.oauth.dto.OauthTokenResponse;
import com.nobrain.bookmarking.domain.oauth.dto.OauthUserProfile;
import com.nobrain.bookmarking.domain.oauth.repository.InMemoryProviderRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OauthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public LoginResponse login(String providerName, String code) {
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        // access token 가져오기
        OauthTokenResponse tokenResponse = getToken(code, provider);

        // 유저 정보 가져오기
        OauthUserProfile oauthUserProfile = getOauthUserProfile(providerName, tokenResponse, provider);

        // 유저 DB에 저장
        User user = saveOrLogin(oauthUserProfile);

        // access token, refresh token 생성 및 저장
        RefreshToken refreshToken = tokenProvider.generateRefreshToken(user.getId());
        String accessToken = tokenProvider.generateAccessToken(refreshToken.getRefreshToken());
        TokenDto tokenDto = new TokenDto(TOKEN_TYPE, accessToken, refreshToken.getRefreshToken());

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .tokenDto(tokenDto)
                .build();
    }

    private User saveOrLogin(OauthUserProfile userProfile) {
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .orElseGet(userProfile::toUser);

        return userRepository.save(user);
    }

    private OauthTokenResponse getToken(String code, OauthProvider provider) {
        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, OauthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }

    private OauthUserProfile getOauthUserProfile(String providerName, OauthTokenResponse tokenResponse, OauthProvider provider) {
        Map<String, Object> userAttributes = getOauthUserAttributes(provider, tokenResponse);
        return OauthAttributes.extract(providerName, userAttributes);
    }

    private Map<String, Object> getOauthUserAttributes(OauthProvider provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getAccountInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
