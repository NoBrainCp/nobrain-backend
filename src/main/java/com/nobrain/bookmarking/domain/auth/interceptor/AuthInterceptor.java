package com.nobrain.bookmarking.domain.auth.interceptor;

import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import com.nobrain.bookmarking.domain.auth.util.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPreflight(request)) {
            return true;
        }

        String token = JwtTokenExtractor.extract(request);
        tokenProvider.validateToken(token);
        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }
}
