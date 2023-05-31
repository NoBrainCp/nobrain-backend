package com.nobrain.bookmarking.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
}
