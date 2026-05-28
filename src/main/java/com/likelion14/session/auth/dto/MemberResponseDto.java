package com.likelion14.session.auth.dto;

import com.likelion14.session.auth.entity.Member;

public record MemberResponseDto(Long id, String email, String name, String role) {

    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getRole() == null ? "USER" : member.getRole().name()
        );
    }
}
