package com.nobrain.bookmarking.domain.oauth;

import com.nobrain.bookmarking.domain.oauth.dto.OauthUserProfile;
import com.nobrain.bookmarking.domain.oauth.exception.OauthProviderInvalidException;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {
    GITHUB("github") {
        @Override
        public OauthUserProfile of(Map<String, Object> attributes) {
            return OauthUserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("id")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("avatar_url"))
                    .build();
        }
    },
    NAVER("naver") {
        @Override
        public OauthUserProfile of(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return OauthUserProfile.builder()
                    .oauthId(String.valueOf(response.get("id")))
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .imageUrl((String) response.get("profile_image"))
                    .build();
        }
    },
    GOOGLE("google") {
        @Override
        public OauthUserProfile of(Map<String, Object> attributes) {
            return OauthUserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("sub")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("picture"))
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(String name) {
        this.providerName = name;
    }

    public static OauthUserProfile extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(() -> new OauthProviderInvalidException(providerName))
                .of(attributes);
    }

    public abstract OauthUserProfile of(Map<String, Object> attributes);
}
