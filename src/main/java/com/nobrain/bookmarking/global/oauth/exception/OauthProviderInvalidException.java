package com.nobrain.bookmarking.global.oauth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.UnauthorizedException;

public class OauthProviderInvalidException extends UnauthorizedException {

    public OauthProviderInvalidException(String value) {
        super(value, ErrorCode.INVALID_OAUTH_PROVIDER);
    }
}
