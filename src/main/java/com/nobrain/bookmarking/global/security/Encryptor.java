package com.nobrain.bookmarking.global.security;

public interface Encryptor {

    String encrypt(String password);
    boolean isMatch(String password, String hashed);
}
