package com.likelion14.session.auth.oauth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.oauth.dto.KakaoTokenResponseDto;
import com.likelion14.session.auth.oauth.dto.KakaoUserInfoResponseDto;
import com.likelion14.session.auth.oauth.dto.SocialLoginResponseDto;
import com.likelion14.session.auth.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientResponseException;

import java.util.Locale;
import java.util.UUID;

@Service
public class KakaoOAuthService {

    private static final String KAKAO_PROVIDER = "kakao";
    private static final String DEFAULT_NAME = "Kakao User";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate = new RestTemplate();

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

    public KakaoOAuthService(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SocialLoginResponseDto login(String code) {
        return kakaoLogin(code);
    }

    @Transactional
    public SocialLoginResponseDto kakaoLogin(String code) {
        String authorizationCode = requireText(code, "authorizationCode");

        String kakaoAccessToken = getKakaoAccessToken(authorizationCode);
        KakaoUserInfoResponseDto userInfo = getKakaoUserInfo(kakaoAccessToken);
        MemberLoginResult loginResult = loginOrSignup(userInfo);

        Member member = loginResult.member();
        String jwt = jwtTokenProvider.createAccessToken(member);
        return new SocialLoginResponseDto(member.getName(), jwt, loginResult.newUser());
    }

    private String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        if (StringUtils.hasText(clientSecret)) {
            body.add("client_secret", clientSecret);
        }

        try {
            ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(
                    tokenUri,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    KakaoTokenResponseDto.class
            );

            KakaoTokenResponseDto tokenResponse = response.getBody();
            if (tokenResponse == null || !StringUtils.hasText(tokenResponse.getAccess_token())) {
                throw new IllegalStateException("Kakao token response was empty.");
            }
            return tokenResponse.getAccess_token();
        } catch (RestClientResponseException ex) {
            throw mapKakaoException("Failed to exchange Kakao authorization code.", ex);
        }
    }

    private KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                    userInfoUri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    KakaoUserInfoResponseDto.class
            );

            KakaoUserInfoResponseDto body = response.getBody();
            if (body == null || body.getId() == null) {
                throw new IllegalStateException("Kakao user info response was empty.");
            }
            return body;
        } catch (RestClientResponseException ex) {
            throw mapKakaoException("Failed to retrieve Kakao user information.", ex);
        }
    }

    private MemberLoginResult loginOrSignup(KakaoUserInfoResponseDto userInfo) {
        String providerId = resolveProviderId(userInfo);
        return memberRepository.findByProviderAndProviderId(KAKAO_PROVIDER, providerId)
                .map(member -> new MemberLoginResult(syncSocialMember(member, userInfo), false))
                .orElseGet(() -> createNewMember(userInfo, providerId));
    }

    private MemberLoginResult createNewMember(KakaoUserInfoResponseDto userInfo, String providerId) {
        String email = resolveEmail(userInfo);
        String name = resolveName(userInfo);

        if (StringUtils.hasText(email)) {
            return memberRepository.findByEmail(email)
                    .map(member -> {
                        if (StringUtils.hasText(name) && !name.equals(member.getName())) {
                            member.updateName(name);
                        }
                        return new MemberLoginResult(member, false);
                    })
                    .orElseGet(() -> new MemberLoginResult(saveNewMember(email, name, providerId), true));
        }

        return new MemberLoginResult(saveNewMember(null, name, providerId), true);
    }

    private Member saveNewMember(String email, String name, String providerId) {
        return memberRepository.save(new Member(
                email,
                passwordEncoder.encode(UUID.randomUUID().toString()),
                name,
                Role.USER,
                KAKAO_PROVIDER,
                providerId
        ));
    }

    private Member syncSocialMember(Member member, KakaoUserInfoResponseDto userInfo) {
        if (member == null) {
            return null;
        }

        String email = resolveEmail(userInfo);
        String name = resolveName(userInfo);

        if (StringUtils.hasText(email) && !email.equals(member.getEmail())) {
            Member sameEmailMember = memberRepository.findByEmail(email).orElse(null);
            if (sameEmailMember == null || sameEmailMember.getId().equals(member.getId())) {
                member.updateEmail(email);
            }
        }

        if (StringUtils.hasText(name) && !name.equals(member.getName())) {
            member.updateName(name);
        }

        return member;
    }

    private String resolveProviderId(KakaoUserInfoResponseDto userInfo) {
        if (userInfo == null || userInfo.getId() == null) {
            throw new IllegalArgumentException("Kakao user id is required.");
        }
        return String.valueOf(userInfo.getId());
    }

    private String resolveEmail(KakaoUserInfoResponseDto userInfo) {
        KakaoUserInfoResponseDto.KakaoAccount kakaoAccount = userInfo.getKakao_account();
        if (kakaoAccount != null && StringUtils.hasText(kakaoAccount.getEmail())) {
            return normalizeEmail(kakaoAccount.getEmail());
        }
        return null;
    }

    private String resolveName(KakaoUserInfoResponseDto userInfo) {
        KakaoUserInfoResponseDto.KakaoAccount kakaoAccount = userInfo.getKakao_account();
        if (kakaoAccount != null) {
            KakaoUserInfoResponseDto.Profile profile = kakaoAccount.getProfile();
            if (profile != null && StringUtils.hasText(profile.getNickname())) {
                return profile.getNickname().trim();
            }
        }

        if (userInfo.getProperties() != null && StringUtils.hasText(userInfo.getProperties().getNickname())) {
            return userInfo.getProperties().getNickname().trim();
        }

        return DEFAULT_NAME;
    }

    private String normalizeEmail(String email) {
        return requireText(email, "email").toLowerCase(Locale.ROOT);
    }

    private String requireText(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    private RuntimeException mapKakaoException(String message, RestClientResponseException ex) {
        if (ex.getStatusCode().is4xxClientError()) {
            return new IllegalArgumentException(message, ex);
        }
        return new IllegalStateException(message, ex);
    }

    private record MemberLoginResult(Member member, boolean newUser) {
    }
}
