package com.nobrain.bookmarking.domain.user.controller;

import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.domain.user.service.UserService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ResponseService responseService;

    @GetMapping("/user/my-profile")
    public SingleResult<UserResponse.Profile> getMyProfile() {
        return responseService.getSingleResult(userService.getMyProfile());
    }

    @GetMapping("/user/username/{username}/exists")
    public SingleResult<Boolean> existsUsername(@PathVariable String username) {
        return responseService.getSingleResult(userRepository.existsByName(username));
    }

    @GetMapping("/user/login-id/{loginId}/exists")
    public SingleResult<Boolean> existsLoginId(@PathVariable String loginId) {
        return responseService.getSingleResult(userRepository.existsByLoginId(loginId));
    }

    @PutMapping("/user/{userId}/username")
    public SingleResult<Long> changeName(@PathVariable Long userId, @RequestBody UserRequest.ChangeUserName dto) {
        return responseService.getSingleResult(userService.changeName(userId, dto));
    }

    @PutMapping("/user/password")
    public CommonResult changePassword(@RequestBody UserRequest.ChangePassword dto) {
        userService.changePassword(dto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/user/{userId}")
    public CommonResult delete(@PathVariable Long userId) {
        userService.delete(userId);
        return responseService.getSuccessResult();
    }
}
