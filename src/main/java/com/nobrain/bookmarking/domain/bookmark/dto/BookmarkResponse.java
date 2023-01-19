package com.nobrain.bookmarking.domain.bookmark.dto;

public class BookmarkResponse {

    public static class Info {
        private String url;
        private String title;
        private String description;
        private boolean isPublic;
        private boolean isStar;
    }
}
