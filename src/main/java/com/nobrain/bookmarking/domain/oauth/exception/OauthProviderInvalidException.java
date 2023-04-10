package com.nobrain.bookmarking.domain.oauth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.UnauthorizedException;

public class OauthProviderInvalidException extends UnauthorizedException {

    public OauthProviderInvalidException(String value) {
        super(value, ErrorCode.INVALID_OAUTH_PROVIDER);
    }
}
