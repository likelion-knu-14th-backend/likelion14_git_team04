package com.likelion14.session.auth.oauth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.oauth.dto.GoogleTokenResponseDto;
import com.likelion14.session.auth.oauth.dto.GoogleUserInfoResponseDto;
import com.likelion14.session.auth.oauth.dto.SocialLoginResponseDto;
import com.likelion14.session.entity.FoodStore;
import com.likelion14.session.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final FoodStoreRepository foodStoreRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Value("${google.token-uri}")
    private String tokenUri;

    @Value("${google.user-info-uri}")
    private String userInfoUri;

    public SocialLoginResponseDto googleLogin(String code) {
        String googleAccessToken = getGoogleAccessToken(code);
        GoogleUserInfoResponseDto userInfo = getGoogleUserInfo(googleAccessToken);
        return loginOrSignup(userInfo);
    }

    private String getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        System.out.println("clientId = " + clientId);
        System.out.println("redirectUri = " + redirectUri);
        System.out.println("code = " + code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<GoogleTokenResponseDto> response = restTemplate.postForEntity(
                tokenUri,
                request,
                GoogleTokenResponseDto.class
        );

        return response.getBody().getAccess_token();
    }

    private GoogleUserInfoResponseDto getGoogleUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoResponseDto> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                GoogleUserInfoResponseDto.class
        );

        GoogleUserInfoResponseDto body = response.getBody();

        System.out.println("userInfo id = " + (body != null ? body.getId() : null));
        System.out.println("userInfo email = " + (body != null ? body.getEmail() : null));
        System.out.println("userInfo name = " + (body != null ? body.getName() : null));

        return body;
    }

    private SocialLoginResponseDto loginOrSignup(GoogleUserInfoResponseDto userInfo) {
        String provider = "GOOGLE";
        String providerId = String.valueOf(userInfo.getId());

        String email = userInfo.getEmail();
        String nickname = userInfo.getName() != null ? userInfo.getName() : "구글사용자";

        String finalEmail = (email != null && !email.isBlank())
                ? email
                : "google_" + providerId + "@google.local";

        return foodStoreRepository.findByProviderAndProviderId(provider, providerId)
                .map(foodStore -> {
                    String jwt = jwtTokenProvider.generateToken(foodStore);
                    return new SocialLoginResponseDto(foodStore.getName(), jwt, false);
                })
                .orElseGet(() -> {
                    FoodStore newFoodStore = new FoodStore();
                    newFoodStore.setName(nickname);
                    newFoodStore.setEmail(finalEmail);

                    newFoodStore.setTel("GOOGLE_" + providerId);

                    newFoodStore.setRole(Role.OWNER);
                    newFoodStore.setProvider(provider);
                    newFoodStore.setProviderId(providerId);
                    newFoodStore.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

                    FoodStore savedFoodStore = foodStoreRepository.save(newFoodStore);
                    String jwt = jwtTokenProvider.generateToken(savedFoodStore);

                    return new SocialLoginResponseDto(savedFoodStore.getName(), jwt, true);
                });
    }
}