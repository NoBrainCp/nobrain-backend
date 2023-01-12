package com.nobrain.bookmarking.global.response.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    private boolean success;
    private int code;
    private String message;
}
