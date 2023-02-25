package com.nobrain.bookmarking.domain.certification.mail.dto;

import lombok.Getter;

public class MailRequest {

    @Getter
    public static class Authentication {
        private String code;
    }
}
