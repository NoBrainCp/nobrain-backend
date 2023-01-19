package com.nobrain.bookmarking.domain.category.dto;

import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.Getter;

public class CategoryRequest {

    @Getter
    public static class Create {
        private String name;
        private String username;

        public Category toEntity(User user) {
            return Category.builder()
                    .name(this.name)
                    .user(user)
                    .build();
        }
    }
}
