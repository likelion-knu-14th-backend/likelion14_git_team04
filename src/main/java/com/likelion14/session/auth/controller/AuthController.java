package com.likelion14.session.auth.controller;

import com.likelion14.session.auth.dto.LoginRequestDto;
import com.likelion14.session.auth.dto.SignupRequestDto;
import com.likelion14.session.auth.dto.TokenResponseDto;
import com.likelion14.session.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
}
