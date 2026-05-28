package com.likelion14.session.auth.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoTokenResponseDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
}