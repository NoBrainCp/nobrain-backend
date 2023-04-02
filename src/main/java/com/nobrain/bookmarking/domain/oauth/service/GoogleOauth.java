package com.nobrain.bookmarking.domain.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobrain.bookmarking.domain.oauth.dto.GoogleOAuthToken;
import com.nobrain.bookmarking.domain.oauth.dto.GoogleUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleOauth implements SocialOauth {

    @Value("${spring.oauth2.google.url}")
    private String GOOGLE_LOGIN_URL;

    @Value("${spring.oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${spring.oauth2.google.callback-url}")
    private String GOOGLE_CALLBACK_URL;

    @Value("${spring.oauth2.google.scope}")
    private String GOOGLE_ACCESS_SCOPE;

    private final ObjectMapper objectMapper;

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", GOOGLE_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_LOGIN_URL + "?" + parameterString;
        log.info("redirect URL={}", redirectURL);

        return redirectURL;
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        RestTemplate restTemplate=new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity=restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params,String.class);

        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity;
        }
        return null;
    }

    public GoogleOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() = {}", response.getBody());
        return objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        log.info("response.getBody() = {}" + response.getBody());
        return response;
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
        return objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
    }
}
