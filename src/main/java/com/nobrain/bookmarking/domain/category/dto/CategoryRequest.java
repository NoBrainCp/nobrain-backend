package com.nobrain.bookmarking.domain.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.Getter;

public class CategoryRequest {

    @Getter
    public static class Create {
        private String name;
        private String description;

        @JsonProperty("isPublic")
        private boolean isPublic;
        private String username;

        public Category toEntity(User user) {
            return Category.builder()
                    .name(this.name)
                    .description(description)
                    .isPublic(isPublic)
                    .user(user)
                    .build();
        }
    }
}
