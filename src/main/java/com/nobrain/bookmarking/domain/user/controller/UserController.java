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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @GetMapping("/user/{username}/info")
    public SingleResult<UserResponse.Info> getUserInfo(@PathVariable String username) {
        return responseService.getSingleResult(userService.getUserInfo(username));
    }

    @GetMapping("/user/username/{username}/exists")
    public SingleResult<Boolean> existsUsername(@PathVariable String username) {
        return responseService.getSingleResult(userRepository.existsByName(username));
    }

    @GetMapping("/user/login-id/{loginId}/exists")
    public SingleResult<Boolean> existsLoginId(@PathVariable String loginId) {
        return responseService.getSingleResult(userRepository.existsByLoginId(loginId));
    }

    @PutMapping("/user/{userId}/username/{username}")
    public SingleResult<String> changeName(@PathVariable Long userId, @PathVariable String username) {
        return responseService.getSingleResult(userService.changeName(userId, username));
    }

    @PutMapping("/user/forgot-password")
    public CommonResult changeForgotPassword(@RequestBody UserRequest.ChangeForgotPassword dto) {
        userService.changeForgotPassword(dto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/user/password")
    public CommonResult changePassword(@RequestBody UserRequest.ChangePassword dto) {
        userService.changePassword(dto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/user/profile-image")
    public CommonResult changeProfileImage(@RequestBody MultipartFile image) throws IOException {
        userService.changeProfileImage(image);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/user/{userId}")
    public CommonResult delete(@PathVariable Long userId) {
        userService.delete(userId);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/user/profile-image")
    public CommonResult deleteProfileImage() {
        userService.deleteProfileImage();
        return responseService.getSuccessResult();
    }
}
