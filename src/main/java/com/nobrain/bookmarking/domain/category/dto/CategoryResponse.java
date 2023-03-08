package com.nobrain.bookmarking.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CategoryResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Long id;
        private String name;
        private String description;
        private boolean isPublic;
        private Long count;
    }
}
