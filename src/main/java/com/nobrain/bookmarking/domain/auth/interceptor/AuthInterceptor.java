package com.nobrain.bookmarking.domain.auth.interceptor;

import com.nobrain.bookmarking.domain.auth.exception.TokenInvalidException;
import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (hasAuthorization(request)) {
            validateAuthorization(request);
            return true;
        }

        return true;
    }

    private boolean hasAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    private void validateAuthorization(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        if (!tokenProvider.validateToken(token)) {
            throw new TokenInvalidException(token);
        }
    }
}
