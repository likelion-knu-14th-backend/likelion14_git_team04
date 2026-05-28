package com.likelion14.session.auth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.dto.LoginRequestDto;
import com.likelion14.session.auth.dto.MemberResponseDto;
import com.likelion14.session.auth.dto.SignupRequestDto;
import com.likelion14.session.auth.dto.TokenResponseDto;
import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.exception.AlreadyEmailExistsException;
import com.likelion14.session.auth.exception.InvalidPasswordException;
import com.likelion14.session.auth.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Service
public class AuthService {

    private static final String INVALID_LOGIN_MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public MemberResponseDto signup(SignupRequestDto request) {
        validateSignupRequest(request);

        String email = normalizeEmail(request.email());
        if (memberRepository.existsByEmail(email)) {
            throw new AlreadyEmailExistsException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member(
                email,
                passwordEncoder.encode(request.password().trim()),
                request.name().trim(),
                Role.USER
        );
        Member savedMember = memberRepository.save(member);
        return MemberResponseDto.from(savedMember);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto request) {
        validateLoginRequest(request);

        String email = normalizeEmail(request.email());
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidPasswordException(INVALID_LOGIN_MESSAGE));

        if (!passwordEncoder.matches(request.password().trim(), member.getPassword())) {
            throw new InvalidPasswordException(INVALID_LOGIN_MESSAGE);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member);
        return TokenResponseDto.of(
                accessToken,
                jwtTokenProvider.getExpirationSeconds(),
                MemberResponseDto.from(member)
        );
    }

    private void validateSignupRequest(SignupRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("요청 본문이 필요합니다.");
        }
        requireText(request.email(), "email");
        requireText(request.password(), "password");
        requireText(request.name(), "name");
        if (request.password().trim().length() < 6) {
            throw new IllegalArgumentException("password는 6자 이상이어야 합니다.");
        }
    }

    private void validateLoginRequest(LoginRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("요청 본문이 필요합니다.");
        }
        requireText(request.email(), "email");
        requireText(request.password(), "password");
    }

    private String normalizeEmail(String email) {
        return requireText(email, "email").toLowerCase(Locale.ROOT);
    }

    private String requireText(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(fieldName + " 값은 필수입니다.");
        }
        return value.trim();
    }
}
