package com.nobrain.bookmarking.domain.follow.controller;

import com.nobrain.bookmarking.domain.follow.service.FollowService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    private final ResponseService responseService;

    @PostMapping("/{toUserId}")
    public CommonResult follow(@PathVariable Long toUserId) {
        followService.follow(toUserId);
        return responseService.getSuccessResult();
    }
}
