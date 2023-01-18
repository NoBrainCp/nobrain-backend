package com.nobrain.bookmarking.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BookmarkRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class AddBookmarkRequest {
        private String url;
        private String title;
        private String description;

        @JsonProperty("isPublic")
        private boolean isPublic;

        @JsonProperty("isStar")
        private boolean isStar;

        private String categoryName;
        private String tags;

        public Bookmark toEntity(User user, Category category) {
            return Bookmark.builder()
                    .url(this.url)
                    .title(this.title)
                    .description(this.description)
                    .isPublic(this.isPublic)
                    .isStar(this.isStar)
                    .user(user)
                    .category(category)
                    .tags(Arrays.stream(this.tags.split(" ")).map((tagName) -> Tag.builder().name(tagName).build()).collect(Collectors.toSet()))
                    .build();
        }
    }
}
