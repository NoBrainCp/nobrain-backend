package com.nobrain.bookmarking.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class BookmarkRequest {

    @Getter
    public static class Info {
        
        @NotEmpty(message = "url-empty")
        @NotBlank(message = "url-blank")
        private String url;

        @NotEmpty(message = "title-empty")
        @NotBlank(message = "title-blank")
        private String title;
        private String description;

        @JsonProperty("isPublic")
        private boolean isPublic;

        @JsonProperty("isStar")
        private boolean isStar;

        private String categoryName;
        private String metaImage;
        private List<String> tags;

        public void addHttpsToUrl(String url) {
            this.url = new StringBuilder(url).insert(0, "https://").toString();
        }

        public Bookmark toEntity(String metaImage, Category category) {
            return Bookmark.builder()
                    .url(this.url)
                    .title(this.title)
                    .description(this.description)
                    .isPublic(this.isPublic)
                    .isStar(this.isStar)
                    .metaImage(metaImage)
                    .category(category)
                    .build();
        }
    }
}
