package com.nobrain.bookmarking.global.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;

    @Value("${jasypt.encryptor.pool-size}")
    private int poolSize;

    @Value("${jasypt.encryptor.output-type}")
    private String outputType;

    @Value("${jasypt.encryptor.iterations}")
    private int iterations;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(algorithm);
        encryptor.setPoolSize(poolSize);
        encryptor.setStringOutputType(outputType);
        encryptor.setKeyObtentionIterations(iterations);
        return encryptor;
    }
}
