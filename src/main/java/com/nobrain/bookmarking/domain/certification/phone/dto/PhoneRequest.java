package com.nobrain.bookmarking.domain.certification.phone.dto;

import lombok.Getter;

import java.util.List;

public class PhoneRequest {

    @Getter
    public static class MultipleMessage {
        private List<String> phoneNumbers;
        private String text;
    }

}
