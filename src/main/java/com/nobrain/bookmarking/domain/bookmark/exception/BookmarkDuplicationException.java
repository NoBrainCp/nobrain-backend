package com.nobrain.bookmarking.domain.bookmark.exception;

import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class BookmarkDuplicationException extends InvalidValueException {

    public BookmarkDuplicationException(String value) {
        super(value);
    }
}
