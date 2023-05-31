package com.nobrain.bookmarking.domain.oauth.controller;

import com.nobrain.bookmarking.domain.auth.dto.LoginResponse;
import com.nobrain.bookmarking.domain.oauth.service.OauthService;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/oauth2")
public class OauthController {

    private final OauthService oauthService;
    private final ResponseService responseService;

    @GetMapping("/login/{provider}")
    public SingleResult<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        return responseService.getSingleResult(oauthService.login(provider, code));
    }
}
