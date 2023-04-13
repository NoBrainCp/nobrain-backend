package com.nobrain.bookmarking.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class BookmarkRequest {

    @Getter
    public static class Info {

        @NotBlank(message = "url은 필수 입력 항목입니다.")
        private String url;

        @NotBlank(message = "title은 필수 입력 항목입니다.")
        @Size(max = 40, message = "title의 최대 길이는 40글자 입니다.")
        private String title;

        @Size(max = 55, message = "description의 최대 길이는 55글자 입니다.")
        private String description;

        @NotBlank(message = "공개/비공개 선택은 필수 항목입니다.")
        @JsonProperty("isPublic")
        private boolean isPublic;

        @NotBlank(message = "즐겨찾기 선택은 필수 항목입니다.")
        @JsonProperty("isStarred")
        private boolean isStarred;

        @NotBlank(message = "카테고리는 필수 입력 항목입니다.")
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
                    .isStarred(this.isStarred)
                    .metaImage(metaImage)
                    .category(category)
                    .build();
        }
    }
}
