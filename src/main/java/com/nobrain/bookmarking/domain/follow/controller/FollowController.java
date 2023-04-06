package com.nobrain.bookmarking.domain.follow.controller;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.nobrain.bookmarking.domain.follow.service.FollowService;
import com.nobrain.bookmarking.domain.user.annotation.VerifiedUser;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/users")
public class FollowController {

    private final FollowService followService;
    private final ResponseService responseService;

    @GetMapping("/{username}/follow-cnt")
    public SingleResult<FollowResponse.FollowCount> getFollowCount(@PathVariable String username) {
        return responseService.getSingleResult(followService.getFollowCount(username));
    }

    @GetMapping("/{username}/followers")
    public ListResult<FollowResponse.Info> getFollowerList(@PathVariable String username) {
        return responseService.getListResult(followService.getFollowerList(username));
    }

    @GetMapping("/{username}/followings")
    public ListResult<FollowResponse.Info> getFollowingList(@PathVariable String username) {
        return responseService.getListResult(followService.getFollowingList(username));
    }

    @GetMapping("/{username}/follower-cards")
    public ListResult<FollowResponse.FollowCard> getFollowerCardList(@VerifiedUser UserPayload myPayload,
                                                                     @PathVariable String username) {
        return responseService.getListResult(followService.getFollowerCardList(myPayload, username));
    }

    @GetMapping("/{username}/following-cards")
    public ListResult<FollowResponse.FollowCard> getFollowingCardList(@VerifiedUser UserPayload myPayload,
                                                                      @PathVariable String username) {
        return responseService.getListResult(followService.getFollowingCardList(myPayload, username));
    }

    @GetMapping("/{toUserId}/is-follow")
    public SingleResult<Boolean> isFollow(@VerifiedUser UserPayload myPayload,
                                          @PathVariable Long toUserId) {
        return responseService.getSingleResult(followService.isFollow(myPayload, toUserId));
    }

    @PostMapping("/{toUserId}/follow")
    public CommonResult follow(@VerifiedUser UserPayload myPayload,
                               @PathVariable Long toUserId) {
        followService.follow(myPayload, toUserId);
        return responseService.getSuccessResult();
    }
}
