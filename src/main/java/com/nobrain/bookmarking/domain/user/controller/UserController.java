package com.nobrain.bookmarking.domain.user.controller;

import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
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

    private final UserService userService;
    private final ResponseService responseService;

    @GetMapping("/profile/my")
    public SingleResult<UserResponse.Profile> getMyProfile() {
        return responseService.getSingleResult(userService.getMyProfile());
    }

    @GetMapping("/username/{username}/exists")
    public SingleResult<Boolean> existsUsername(@PathVariable String username) {
        return responseService.getSingleResult(userService.existsUsername(username));
    }

    @GetMapping("/loginId/{loginId}/exists")
    public SingleResult<Boolean> existsLoginId(@PathVariable String loginId) {
        return responseService.getSingleResult(userService.existsLoginId(loginId));
    }

    @GetMapping("/email/{email}/exists")
    public SingleResult<Boolean> existsEmail(@PathVariable String email) {
        return responseService.getSingleResult(userService.existsEmail(email));
    }
  
    @PostMapping("/find/loginId/email")
    public SingleResult<String> findLoginIdByPhoneEmail(@RequestBody UserRequest.FindLoginIdByEmail request){
        return responseService.getSingleResult(userService.findForgotLoginIdByEmail(request));
    }
  
    @PostMapping("/find/loginId/phone-number")
    public SingleResult<String> findLoginIdByPhoneNumber(@RequestBody UserRequest.FindLoginIdByPhoneNumber request){
        return responseService.getSingleResult(userService.findForgotLoginIdByPhoneNumber(request));
    }

    @PutMapping("/user")
    public SingleResult<Long> update(
            @RequestParam Long userId,
            @RequestParam String username) {
        return responseService.getSingleResult(userService.update(userId, username));
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
