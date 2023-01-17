package com.nobrain.bookmarking.domain.user.controller;

import com.nobrain.bookmarking.domain.user.dto.request.UserSignInRequest;
import com.nobrain.bookmarking.domain.user.dto.request.UserSignUpRequest;
import com.nobrain.bookmarking.domain.user.service.UserSignService;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserSignController {

    private final UserSignService userService;
    private final ResponseService responseService;

    @PostMapping("/signup")
    public SingleResult<Long> signUp(@Valid @RequestBody UserSignUpRequest dto) {
        return responseService.getSingleResult(userService.signUp(dto));
    }

    /**
     * @return accessToken
     */
    @PostMapping("/signin")
    public SingleResult<String> signIn(@Valid @RequestBody UserSignInRequest dto) {
        return responseService.getSingleResult(userService.signIn(dto));
    }
}
