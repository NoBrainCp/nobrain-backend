package com.nobrain.bookmarking.domain.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {

        @NotBlank(message = "카테고리 이름은 필수 항목입니다.")
        @Size(max = 20, message = "이름의 최대 길이는 10글자 입니다.")
        private String name;

        @Size(max = 40, message = "설명의 최대 길이는 30글자 입니다.")
        private String description;

        @NotNull
        @JsonProperty("isPublic")
        private Boolean isPublic;

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
