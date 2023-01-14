package com.nobrain.bookmarking.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignInRequest {
    private String loginId;
    private String password;
}
