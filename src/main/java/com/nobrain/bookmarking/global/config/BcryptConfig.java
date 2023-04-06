package com.nobrain.bookmarking.global.config;

import com.nobrain.bookmarking.global.security.Encryptor;
import com.nobrain.bookmarking.global.security.PasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BcryptConfig {

    @Bean
    public Encryptor encryptor() {
        return new PasswordEncryptor();
    }
}
