package com.likelion14.session.auth.dto;

import com.likelion14.session.auth.common.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {
    // 로그인한 사용자 이름
    private String name;
    // 발급된 JWT
    private String accessToken;



}