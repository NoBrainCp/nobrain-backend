package com.nobrain.bookmarking.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import lombok.Getter;

import java.util.List;

public class BookmarkRequest {

    @Getter
    public static class Info {
        private String url;
        private String title;
        private String description;

        @JsonProperty("isPublic")
        private boolean isPublic;

        @JsonProperty("isStar")
        private boolean isStar;

        private String categoryName;
        private List<String> tags;

        public void setUrl(String url) {
            this.url = new StringBuilder(url).insert(0, "https://").toString();
        }

        public Bookmark toEntity(Category category) {
            return Bookmark.builder()
                    .url(this.url)
                    .title(this.title)
                    .description(this.description)
                    .isPublic(this.isPublic)
                    .isStar(this.isStar)
                    .category(category)
                    .build();
        }
    }
}
