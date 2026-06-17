package com.likelion14.session.auth.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponseDto {
    private String name;
    private String accessToken;
    private boolean isNewUser;
}