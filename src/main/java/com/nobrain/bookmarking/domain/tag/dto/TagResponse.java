package com.nobrain.bookmarking.domain.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public class TagResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Long tagId;
        private String tagName;
    }
}
