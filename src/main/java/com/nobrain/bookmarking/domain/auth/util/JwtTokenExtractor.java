package com.nobrain.bookmarking.domain.auth.util;

import com.nobrain.bookmarking.domain.auth.exception.TokenInvalidException;
import com.nobrain.bookmarking.domain.auth.exception.TokenNotExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenExtractor {

    private static final String TOKEN_TYPE = "Bearer";
    private static final int TOKEN_TYPE_INDEX = 0;
    private static final int TOKEN_INDEX = 1;

    public static String extract(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null) {
            throw new TokenNotExistsException("Access Token");
        }

        String[] splitHeader = authorizationHeader.split(" ");
        if (splitHeader.length != 2 || !splitHeader[TOKEN_TYPE_INDEX].equalsIgnoreCase(TOKEN_TYPE)) {
            throw new TokenInvalidException(authorizationHeader);
        }

        return splitHeader[TOKEN_INDEX];
    }
}
