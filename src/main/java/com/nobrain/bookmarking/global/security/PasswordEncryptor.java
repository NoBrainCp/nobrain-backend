package com.nobrain.bookmarking.global.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor implements Encryptor {

    @Override
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
