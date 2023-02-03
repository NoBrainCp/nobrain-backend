package com.nobrain.bookmarking.domain.bookmark.exception;

import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class BookmarkNotFoundException extends InvalidValueException {

    public BookmarkNotFoundException(String value) {
        super(value);
    }
}
