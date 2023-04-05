package com.nobrain.bookmarking.domain.category.dto;

import com.nobrain.bookmarking.domain.category.entity.Category;
import lombok.*;

public class CategoryResponse {


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        private Long id;
        private String name;
        private String description;
        private boolean isPublic;

        public static Header toDto(Category category) {
            return Header.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private Long id;
        private String name;
        private String description;
        private boolean isPublic;
        private Long count;
    }
}
