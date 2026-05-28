package com.likelion14.session.auth.dto;

public record TokenResponseDto(String tokenType, String accessToken, long expiresInSeconds, MemberResponseDto member) {

    public static TokenResponseDto of(String accessToken, long expiresInSeconds, MemberResponseDto member) {
        return new TokenResponseDto("Bearer", accessToken, expiresInSeconds, member);
    }
}
