package com.nobrain.bookmarking.domain.auth.interceptor;

import com.nobrain.bookmarking.domain.auth.exception.TokenExpiredException;
import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        if (isPreflight(request)) {
            return true;
        }

        if (hasAuthorization(request)) {
            validateAuthorization(request);
            return true;
        }

        return false;
    }

    private boolean isPreflight(final HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean hasAuthorization(final HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    private void validateAuthorization(final HttpServletRequest request) {
        if (!tokenProvider.validateToken(request)) {
            throw new TokenExpiredException("Access Token");
        }
    }
}
