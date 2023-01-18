package com.nobrain.bookmarking.domain.category.dto.request;

import com.nobrain.bookmarking.domain.category.entity.Category;
import lombok.Getter;

public class CategoryRequest {

    @Getter
    public static class Create {
        private String name;

        public Category toEntity() {
            return Category.builder()
                    .name(this.name)
                    .build();
        }
    }
}
