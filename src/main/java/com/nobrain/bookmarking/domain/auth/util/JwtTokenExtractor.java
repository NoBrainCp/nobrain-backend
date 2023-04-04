package com.nobrain.bookmarking.domain.auth.util;

import com.nobrain.bookmarking.domain.auth.exception.TokenInvalidException;
import com.nobrain.bookmarking.domain.auth.exception.TokenNotExistsException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenExtractor {

    public String extract(String authorizationHeader, String tokenType) {
        if (authorizationHeader == null) {
            throw new TokenNotExistsException();
        }

        String[] splitHeader = authorizationHeader.split(" ");
        if (splitHeader.length != 2 || !splitHeader[0].equalsIgnoreCase(tokenType)) {
            throw new TokenInvalidException(authorizationHeader);
        }

        return splitHeader[1];
    }
}
