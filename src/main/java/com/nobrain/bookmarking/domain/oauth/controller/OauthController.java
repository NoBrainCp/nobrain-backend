package com.nobrain.bookmarking.domain.oauth.controller;

import com.nobrain.bookmarking.domain.oauth.service.OauthService;
import com.nobrain.bookmarking.domain.oauth.enums.SocialLoginType;
import com.nobrain.bookmarking.domain.oauth.dto.GoogleUser;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;
    private final ResponseService responseService;

    @GetMapping("/oauth/{socialLoginType}")
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String socialLoginPath) throws IOException {
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        oauthService.request(socialLoginType);
    }

    @GetMapping("/app/accounts/auth/{socialLoginType}/callback")
    public SingleResult<GoogleUser> callback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException {

        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GoogleUser googleUser = oauthService.oAuthLogin(socialLoginType, code);
        return responseService.getSingleResult(googleUser);
    }
}
