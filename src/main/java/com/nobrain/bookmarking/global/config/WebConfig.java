package com.nobrain.bookmarking.global.config;

import com.nobrain.bookmarking.domain.auth.interceptor.AuthInterceptor;
import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
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
    private final AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> pathsToExclude = List.of(
                "/api/v1/auth/login",
                "/api/v1/users/username/*exists",
                "/api/v1/users/login-id/*/exists",
                "/api/v1/users/sign-up"
        );

        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(pathsToExclude);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(tokenProvider));
    }
}
