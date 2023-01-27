package com.nobrain.bookmarking.domain.bookmark.dto;

import lombok.*;

import java.time.LocalDate;

public class BookmarkResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Info {
        private String url;
        private String title;
        private String description;
        private boolean isPublic;
        private boolean isStar;
        private LocalDate createdAt;
    }
}
