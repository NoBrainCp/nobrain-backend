package com.nobrain.bookmarking.domain.oauth.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.domain.oauth.enums.SocialLoginType;
import com.nobrain.bookmarking.domain.oauth.dto.GoogleOAuthToken;
import com.nobrain.bookmarking.domain.oauth.dto.GoogleUser;
import com.nobrain.bookmarking.domain.oauth.exception.NotFoundSocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class OauthService {

    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final UserRepository userRepository;

    public void request(SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                redirectURL = googleOauth.getOauthRedirectURL();
                break;
            } default: {
                throw new NotFoundSocialLoginType(socialLoginType.name());
            }
        }

        response.sendRedirect(redirectURL);
    }

    public GoogleUser oAuthLogin(SocialLoginType socialLoginType, String code) throws IOException {
        switch (socialLoginType) {
            case GOOGLE: {
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                /**
                 * userEmail 존재하지 않을 시 회원가입 철차
                 */
                return googleUser;
            } default: {
                throw new NotFoundSocialLoginType(socialLoginType.name());
            }
        }
    }
}
