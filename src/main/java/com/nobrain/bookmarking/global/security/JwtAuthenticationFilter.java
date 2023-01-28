package com.nobrain.bookmarking.global.security;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenService tokenService;

    // Request로 들어오는 Jwt Token의 유효성 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = tokenService.resolveToken((HttpServletRequest) request); // 헤더에서 JWT 받아옴
        if (token != null && tokenService.validateToken(token)) { // 유효한 토큰인지 확인
            Authentication authentication = tokenService.getAuthentication(token); // 토큰으로 부터 유저 정보를 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 객체를 저장
        }

        chain.doFilter(request, response);
    }
}
