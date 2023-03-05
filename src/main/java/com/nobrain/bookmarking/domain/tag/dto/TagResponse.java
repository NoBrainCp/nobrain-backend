package com.nobrain.bookmarking.domain.tag.dto;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;

import java.util.List;

public class TagResponse {

    public static class Info {
        private String name;
        private List<Bookmark> bookmarks;
    }
}
