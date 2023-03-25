package com.nobrain.bookmarking.infra.certification.mail.dto;

import lombok.Getter;

import java.util.List;

public class MailRequest {

    @Getter
    public static class MultipleMail {
        private List<String> mails;
        private String subject;
        private String text;
    }
}
