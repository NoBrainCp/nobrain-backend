package com.nobrain.bookmarking.global.jasypt;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

//@SpringBootTest
class JasyptConfigTest {

    @Test
    void jasypt() {
        String url = "test:mysql";
        String username = "test";
        String password = "test1234!";

        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);

        System.out.println(encryptUrl);
        System.out.println(encryptUsername);
        System.out.println(encryptPassword);

        Assertions.assertThat(url).isEqualTo(jasyptDecrypt(encryptUrl));
        Assertions.assertThat(username).isEqualTo(jasyptDecrypt(encryptUsername));
        Assertions.assertThat(password).isEqualTo(jasyptDecrypt(encryptPassword));
    }

    private String jasyptEncrypt(String input) {
        String key = "testKey";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecrypt(String input) {
        String key = "testKey";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }
}