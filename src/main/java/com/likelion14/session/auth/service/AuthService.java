package com.likelion14.session.auth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.dto.LoginRequestDto;
import com.likelion14.session.auth.dto.SignupRequestDto;
import com.likelion14.session.auth.dto.TokenResponseDto;
import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion14.session.auth.exception.AlreadyEmailExistsException;
import com.likelion14.session.auth.exception.InvalidPasswordException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequestDto request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyEmailExistsException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                Role.USER,
                "local",
                null
        );

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member);
        return new TokenResponseDto(accessToken);
    }
}
