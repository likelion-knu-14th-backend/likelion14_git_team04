package com.likelion14.session.auth.controller;

import com.likelion14.session.auth.common.SecurityUtil;
import com.likelion14.session.auth.dto.LoginRequestDto;
import com.likelion14.session.auth.dto.MemberResponseDto;
import com.likelion14.session.auth.dto.SignupRequestDto;
import com.likelion14.session.auth.dto.TokenResponseDto;
import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Sign up")
    @PostMapping("/signup")
    public MemberResponseDto signup(@RequestBody SignupRequestDto request) {
        return authService.signup(request);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @Operation(summary = "My profile")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public MemberResponseDto me() {
        Member member = SecurityUtil.getCurrentMember();
        return MemberResponseDto.from(member);
    }
}
