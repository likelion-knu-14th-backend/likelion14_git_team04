package com.likelion14.session.auth.oauth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.oauth.dto.KakaoTokenResponseDto;
import com.likelion14.session.auth.oauth.dto.KakaoUserInfoResponseDto;
import com.likelion14.session.auth.oauth.dto.SocialLoginResponseDto;
import com.likelion14.session.entity.Student;
import com.likelion14.session.repository.StudentRepository;
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
public class KakaoOAuthService {

    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    public SocialLoginResponseDto kakaoLogin(String code) {
        String kakaoAccessToken = getKakaoAccessToken(code);
        KakaoUserInfoResponseDto userInfo = getKakaoUserInfo(kakaoAccessToken);
        return loginOrSignup(userInfo);
    }

    private String getKakaoAccessToken(String code) {
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

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                tokenUri,
                request,
                KakaoTokenResponseDto.class
        );

        return response.getBody().getAccess_token();
    }

    private KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                KakaoUserInfoResponseDto.class
        );

        KakaoUserInfoResponseDto body = response.getBody();

        System.out.println("userInfo id = " + (body != null ? body.getId() : null));
        System.out.println("userInfo kakao_account = " + (body != null ? body.getKakao_account() : null));
        System.out.println("userInfo properties = " + (body != null ? body.getProperties() : null));

        return body;
    }

    private SocialLoginResponseDto loginOrSignup(KakaoUserInfoResponseDto userInfo) {
        String provider = "KAKAO";
        String providerId = String.valueOf(userInfo.getId());

        String email = null;
        String nickname = "카카오사용자";

        if (userInfo.getKakao_account() != null) {
            email = userInfo.getKakao_account().getEmail();

            if (userInfo.getKakao_account().getProfile() != null
                    && userInfo.getKakao_account().getProfile().getNickname() != null) {
                nickname = userInfo.getKakao_account().getProfile().getNickname();
            }
        }

        if (userInfo.getProperties() != null
                && userInfo.getProperties().getNickname() != null) {
            nickname = userInfo.getProperties().getNickname();
        }

        String finalEmail = (email != null && !email.isBlank())
                ? email
                : "kakao_" + providerId + "@kakao.local";

        String finalNickname = nickname;

        return studentRepository.findByProviderAndProviderId(provider, providerId)
                .map(student -> {
                    String jwt = jwtTokenProvider.generateToken(student);
                    return new SocialLoginResponseDto(student.getName(), jwt, false);
                })
                .orElseGet(() -> {
                    Student newStudent = new Student();
                    newStudent.setName(finalNickname);
                    newStudent.setEmail(finalEmail);
                    newStudent.setStudentNumber("KAKAO_" + providerId);
                    newStudent.setAge(0);
                    newStudent.setMajor("미정");
                    newStudent.setRole(Role.STUDENT);
                    newStudent.setProvider(provider);
                    newStudent.setProviderId(providerId);
                    newStudent.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

                    Student savedStudent = studentRepository.save(newStudent);
                    String jwt = jwtTokenProvider.generateToken(savedStudent);

                    return new SocialLoginResponseDto(savedStudent.getName(), jwt, true);
                });
    }
}