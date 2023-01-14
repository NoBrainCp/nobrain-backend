package com.nobrain.bookmarking.domain.category.dto.request;

import com.nobrain.bookmarking.domain.category.entity.Category;
import lombok.Getter;

@Getter
public class AddCategoryRequest {

    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(this.name)
                .build();
    }
}
