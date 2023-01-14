package com.nobrain.bookmarking.domain.user;

import com.nobrain.bookmarking.domain.user.dto.request.UserSignInRequest;
import com.nobrain.bookmarking.domain.user.dto.request.UserSignUpRequest;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping("/signup")
    public CommonResult signUp(@Valid @RequestBody UserSignUpRequest dto) {
        userService.signUp(dto);
        return responseService.getSuccessResult();
    }

    @PostMapping("/signin")
    public CommonResult signIn(@Valid @RequestBody UserSignInRequest dto) {
        SingleResult<String> result = new SingleResult<>();
        result.setData(userService.signIn(dto));
        responseService.setSuccessResult(result);
        return result;
    }
}
