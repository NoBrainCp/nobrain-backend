package com.nobrain.bookmarking.global.config;

import com.nobrain.bookmarking.domain.user.resolver.LoginUserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenService tokenService;

    public WebConfig(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserIdArgumentResolver(tokenService));
    }
}
