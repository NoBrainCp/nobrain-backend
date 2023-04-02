package com.nobrain.bookmarking.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleOAuthToken {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
