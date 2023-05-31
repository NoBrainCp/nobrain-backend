package com.nobrain.bookmarking.domain.auth.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload {

    private Long userId;
    private String username;
    private List<String> roles;
}
