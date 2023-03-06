package com.nobrain.bookmarking.domain.bookmark_tag.dto.projection;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BookmarkTagProjection {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class BookmarkAndTag {
        private Tag tag;
        private Bookmark bookmark;
    }
}
