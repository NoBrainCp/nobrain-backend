package com.nobrain.bookmarking.domain.user;

import com.nobrain.bookmarking.domain.user.dto.request.UserSignupRequest;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping("/signup")
    public CommonResult signup(@Valid @RequestBody UserSignupRequest dto) {
        userService.signUp(dto);
        return responseService.getSuccessResult();
    }
}
