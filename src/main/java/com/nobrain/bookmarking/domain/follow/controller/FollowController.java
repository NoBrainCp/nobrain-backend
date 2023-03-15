package com.nobrain.bookmarking.domain.follow.controller;

import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.nobrain.bookmarking.domain.follow.service.FollowService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class FollowController {

    private final FollowService followService;
    private final ResponseService responseService;

    @GetMapping("/user/{username}/follow-cnt")
    public SingleResult<FollowResponse.FollowCount> getFollowCount(@PathVariable String username) {
        return responseService.getSingleResult(followService.getFollowCount(username));
    }

    @GetMapping("/user/{username}/followers")
    public ListResult<FollowResponse.Info> getFollowerList(@PathVariable String username) {
        return responseService.getListResult(followService.getFollowerList(username));
    }

    @GetMapping("/user/{username}/followings")
    public ListResult<FollowResponse.Info> getFollowingList(@PathVariable String username) {
        return responseService.getListResult(followService.getFollowingList(username));
    }

    @PostMapping("/user/{toUserId}/follow")
    public CommonResult follow(@PathVariable Long toUserId) {
        followService.follow(toUserId);
        return responseService.getSuccessResult();
    }
}
