package com.nobrain.bookmarking.global.config;

import com.nobrain.bookmarking.domain.oauth.repository.InMemoryProviderRepository;
import com.nobrain.bookmarking.domain.oauth.OauthAdapter;
import com.nobrain.bookmarking.domain.oauth.OauthProperties;
import com.nobrain.bookmarking.domain.oauth.OauthProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class OauthConfig {

    private final OauthProperties properties;

    public OauthConfig(OauthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository(providers);
    }
}
