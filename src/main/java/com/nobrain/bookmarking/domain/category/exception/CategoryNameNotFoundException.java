package com.nobrain.bookmarking.domain.category.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class CategoryNameNotFoundException extends InvalidValueException {

    public CategoryNameNotFoundException(String categoryName) {
        super(categoryName, ErrorCode.CATEGORY_NAME_NOT_FOUND);
    }
}
