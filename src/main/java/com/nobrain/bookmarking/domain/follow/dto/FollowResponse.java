package com.nobrain.bookmarking.domain.follow.dto;

import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.*;

public class FollowResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FollowCount {
        private Integer followerCnt;
        private Integer followingCnt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private Long userId;
        private String username;
        private String profileImage;

        public static Info toResponse(User user) {
            return new Info(user.getId(), user.getName(), user.getProfileImage());
        }
    }
}
