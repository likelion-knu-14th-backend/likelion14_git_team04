package com.likelion14.session.auth.controllor;

import com.likelion14.session.auth.oauth.dto.SocialLoginResponseDto;
import com.likelion14.session.auth.oauth.service.KakaoOAuthService;
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

    private final KakaoOAuthService kakaoOAuthService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login-page")
    public String loginPage(Model model) {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        return "login-page";
    }

    @GetMapping("/api/v1/auth/kakao/callback-page")
    public String kakaoCallbackPage(@RequestParam String code, Model model) {
        SocialLoginResponseDto response = kakaoOAuthService.kakaoLogin(code);

        model.addAttribute("name", response.getName());
        model.addAttribute("accessToken", response.getAccessToken());
        model.addAttribute("isNewUser", response.isNewUser());

        return "login-success";
    }
}