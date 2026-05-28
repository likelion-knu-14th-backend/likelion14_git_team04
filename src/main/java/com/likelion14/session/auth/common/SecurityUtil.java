package com.likelion14.session.auth.common;

import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.exception.InvalidTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Member member)) {
            throw new InvalidTokenException("인증 정보가 없습니다.");
        }
        return member;
    }

    public static Long getCurrentMemberId() {
        return getCurrentMember().getId();
    }

    public static String getCurrentEmail() {
        return getCurrentMember().getEmail();
    }
}
