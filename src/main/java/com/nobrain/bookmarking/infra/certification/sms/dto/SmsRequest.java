package com.nobrain.bookmarking.infra.certification.sms.dto;

import lombok.Getter;

import java.util.List;

public class SmsRequest {

    @Getter
    public static class SingleText {
        private String text;
    }

    @Getter
    public static class MultipleMessage {
        private List<String> phoneNumbers;
        private String text;
    }

    @Getter
    public static class MmsMessage {
        private String text;
        private String resourcePath;
    }
}
