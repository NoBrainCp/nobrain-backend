package com.nobrain.bookmarking.domain.bookmark_tag.dto;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BookmarkTagDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class BookmarkAndTagId {
        private Tag tag;
        private Bookmark bookmark;
    }
}
