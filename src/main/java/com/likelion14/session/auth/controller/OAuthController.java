package com.likelion14.session.auth.controller;

import com.likelion14.session.auth.oauth.dto.SocialLoginResponseDto;
import com.likelion14.session.auth.oauth.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login-page")
    public String loginPage(Model model) {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=email profile";

        model.addAttribute("googleLoginUrl", googleLoginUrl);
        return "login-page";
    }

    @GetMapping("/api/v1/auth/google/callback-page")
    public String googleCallbackPage(@RequestParam String code, Model model) {
        SocialLoginResponseDto response = googleOAuthService.googleLogin(code);

        model.addAttribute("name", response.getName());
        model.addAttribute("accessToken", response.getAccessToken());
        model.addAttribute("isNewUser", response.isNewUser());

        return "login-success";
    }
}