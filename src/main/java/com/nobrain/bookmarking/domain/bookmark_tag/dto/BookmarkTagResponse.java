package com.nobrain.bookmarking.domain.bookmark_tag.dto;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class BookmarkTagResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Tag tag;
        private List<Bookmark> bookmarks;
    }
}
