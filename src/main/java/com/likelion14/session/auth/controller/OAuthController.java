package com.likelion14.session.auth.controller;

import com.likelion14.session.auth.oauth.dto.SocialLoginResponseDto;
import com.likelion14.session.auth.oauth.service.KakaoOAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final String clientId;
    private final String redirectUri;

    public OAuthController(
            KakaoOAuthService kakaoOAuthService,
            @Value("${kakao.client-id}") String clientId,
            @Value("${kakao.redirect-uri}") String redirectUri
    ) {
        this.kakaoOAuthService = kakaoOAuthService;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    @GetMapping("/login-page")
    public String loginPage(Model model) {
        model.addAttribute("kakaoLoginUrl", buildKakaoLoginUrl());
        model.addAttribute("redirectUri", redirectUri);
        return "login-page";
    }

    @GetMapping("/api/v1/auth/kakao/callback-page")
    public String kakaoCallback(@RequestParam(required = false) String code, Model model) {
        SocialLoginResponseDto response = kakaoOAuthService.kakaoLogin(code);
        model.addAttribute("name", response.getName());
        model.addAttribute("accessToken", response.getAccessToken());
        model.addAttribute("isNewUser", response.isNewUser());
        return "login-success";
    }

    private String buildKakaoLoginUrl() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .build()
                .encode()
                .toUriString();
    }
}
