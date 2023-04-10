package com.nobrain.bookmarking.domain.oauth.dto;

import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.UUID;

@Getter
public class OauthUserProfile {

    private final String oauthId;
    private final String email;
    private final String name;
    private final String imageUrl;

    @Builder
    public OauthUserProfile(String oauthId, String email, String name, String imageUrl) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public User toUser() {
        return User.builder()
                .name(UUID.randomUUID().toString())
                .oauthId(oauthId)
                .email(email)
                .profileImage(imageUrl)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
