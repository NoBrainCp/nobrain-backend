package com.nobrain.bookmarking.domain.user.controller;

import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.service.UserSignService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class UserSignController {

    private final UserSignService userService;
    private final ResponseService responseService;

    @PostMapping("/sign-up")
    public CommonResult signUp(@Valid @RequestBody UserRequest.SignUp dto) {
        userService.signUp(dto);
        return responseService.getSuccessResult();
    }
}
