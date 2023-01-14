package com.nobrain.bookmarking.domain.category.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class CategoryNameDuplicationException extends InvalidValueException {
    public CategoryNameDuplicationException(String name) {
        super(name, ErrorCode.CATEGORY_NAME_DUPLICATION);
    }
}
