package com.nobrain.bookmarking.domain.auth.util;

import com.nobrain.bookmarking.domain.auth.exception.AuthorizationHeaderInvalidException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class JwtTokenExtractor {

    private static final String TOKEN_TYPE = "Bearer";
    private static final int TOKEN_INDEX = 1;
    private static final String BLANK = " ";

    public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(TOKEN_TYPE.toLowerCase())) {
                return value.split(BLANK)[TOKEN_INDEX];
            }
        }

        throw new AuthorizationHeaderInvalidException();
    }

    public static boolean hasAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(TOKEN_TYPE.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
