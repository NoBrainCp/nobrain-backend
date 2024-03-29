package com.nobrain.bookmarking.global.config;

import com.nobrain.bookmarking.domain.auth.interceptor.AuthInterceptor;
import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import com.nobrain.bookmarking.domain.auth.util.JwtTokenExtractor;
import com.nobrain.bookmarking.domain.user.resolver.AuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider tokenProvider;
    private final JwtTokenExtractor tokenExtractor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //TODO white list yaml file 분리!!
        List<String> pathsToExclude = List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/access-token",
                "/api/v1/sign-up",
                "/api/v1/users/username/*/exists",
                "/api/v1/users/email/*/exists",
                "/api/v1/mails/*/auth-code",
                "/api/v1/mails/*/auth-code/*",
                "/api/v1/users/password/reset",

                // Oauth2
                "/api/v1/oauth2/login/github",
                "/api/v1/oauth2/login/google",
                "/api/v1/oauth2/login/naver",
                "/redirect/oauth2/github",
                "/redirect/oauth2/google",
                "/redirect/oauth2/naver"
        );

        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(pathsToExclude);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(tokenProvider, tokenExtractor));
    }
}
