package com.likelion14.session.auth.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String JWT_ALG = "HS256";
    private static final String MEMBER_ID = "memberId";
    private static final long EXPIRATION_MILLIS = 60L * 60L * 1000L;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecretKeySpec secretKeySpec;

    public JwtTokenProvider(@org.springframework.beans.factory.annotation.Value("${jwt.secret}") String secret) {
        this.secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
    }

    public String createAccessToken(Member member) {
        try {
            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", JWT_ALG);
            header.put("typ", "JWT");

            Instant now = Instant.now();
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put(MEMBER_ID, member.getId());
            payload.put("sub", member.getEmail());
            payload.put("name", member.getName());
            payload.put("role", member.getRole() == null ? "USER" : member.getRole().name());
            payload.put("iat", now.getEpochSecond());
            payload.put("exp", now.plusMillis(EXPIRATION_MILLIS).getEpochSecond());

            String encodedHeader = encode(objectMapper.writeValueAsBytes(header));
            String encodedPayload = encode(objectMapper.writeValueAsBytes(payload));
            String signingInput = encodedHeader + "." + encodedPayload;
            String signature = encode(sign(signingInput));
            return signingInput + "." + signature;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JWT 생성에 실패했습니다.", e);
        }
    }

    public Long extractMemberId(String token) {
        Object memberId = parseClaims(token).get(MEMBER_ID);
        if (memberId instanceof Number number) {
            return number.longValue();
        }
        throw new InvalidTokenException("토큰에 회원 정보가 없습니다.");
    }

    public long getExpirationSeconds() {
        return EXPIRATION_MILLIS / 1000;
    }

    private Map<String, Object> parseClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new InvalidTokenException("토큰 형식이 올바르지 않습니다.");
            }

            String signingInput = parts[0] + "." + parts[1];
            byte[] expectedSignature = sign(signingInput);
            byte[] actualSignature = Base64.getUrlDecoder().decode(parts[2]);
            if (!MessageDigest.isEqual(expectedSignature, actualSignature)) {
                throw new InvalidTokenException("토큰 서명이 올바르지 않습니다.");
            }

            Map<String, Object> claims = objectMapper.readValue(
                    Base64.getUrlDecoder().decode(parts[1]),
                    new TypeReference<Map<String, Object>>() {
                    }
            );

            Object exp = claims.get("exp");
            if (!(exp instanceof Number expNumber)) {
                throw new InvalidTokenException("토큰 만료 정보가 없습니다.");
            }

            if (Instant.now().getEpochSecond() >= expNumber.longValue()) {
                throw new InvalidTokenException("토큰이 만료되었습니다.");
            }

            return claims;
        } catch (InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException("토큰이 올바르지 않습니다.", e);
        }
    }

    private byte[] sign(String signingInput) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);
            return mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new InvalidTokenException("JWT 서명 생성에 실패했습니다.", e);
        }
    }

    private String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
