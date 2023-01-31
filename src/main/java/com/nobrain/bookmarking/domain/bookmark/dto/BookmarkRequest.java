package com.nobrain.bookmarking.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class BookmarkRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Create {
        private String url;
        private String title;
        private String description;

        @JsonProperty("isPublic")
        private boolean isPublic;

        @JsonProperty("isStar")
        private boolean isStar;

        private String categoryName;
        private List<String> tags;

        public Bookmark toEntity(Category category) {
            return Bookmark.builder()
                    .url(this.url)
                    .title(this.title)
                    .description(this.description)
                    .isPublic(this.isPublic)
                    .isStar(this.isStar)
                    .category(category)
                    .tags(this.tags.stream()
                            .map((tagName) -> Tag.builder().name(tagName).build())
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
