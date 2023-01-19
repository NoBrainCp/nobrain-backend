package com.nobrain.bookmarking.domain.bookmark.dto;

import com.nobrain.bookmarking.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

public class BookmarkResponse {

    @Builder
    @AllArgsConstructor
    public static class Info {
        private String url;
        private String title;
        private String description;
        private boolean isPublic;
        private boolean isStar;
        private List<Tag> tags;
    }
}
