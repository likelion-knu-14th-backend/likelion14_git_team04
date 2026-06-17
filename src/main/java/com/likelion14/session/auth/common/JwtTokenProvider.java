package com.likelion14.session.auth.common;

import com.likelion14.session.entity.Student;
import com.likelion14.session.exception.StudentNotFoundException;
import com.likelion14.session.repository.StudentRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final StudentRepository studentRepository;
    // 토큰 만료 시간: 1시간
    private final long expirationTime = 1000 * 60 * 60;

    // application.properties의 secret key 주입
    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            StudentRepository studentRepository
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
        this.studentRepository = studentRepository;
    }
    // JWT 생성
    public String generateToken(Student student) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(student.getEmail()) // 토큰 주인
                .claim("studentId", student.getId()) // Payload에 담을 정보
                .claim("role", student.getRole().name()) // Payload에 담을 정보
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256) // Secret Key로 서명
                .compact(); // JWT 문자열 생성
    }

    // JWT에서 이메일 추출
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JWT를 기반으로 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        // JWT에서 이메일 추출
        String email = getEmail(token);
        // 이메일로 학생 조회
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(StudentNotFoundException::new);
        // 권한 정보 생성
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + student.getRole().name())
        );
        // 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(
                student.getEmail(),
                null,
                authorities
        );
    }
}