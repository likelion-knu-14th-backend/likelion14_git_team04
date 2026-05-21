package com.likelion14.session.auth.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 기능 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean // PasswordEncoder 객체를 Spring가 관리하도록 등록
    public PasswordEncoder passwordEncoder() {
        // BCrypt 방식으로 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                // 요청별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger 및 회원가입 API는 인증 없이 접근 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/api/v1/auth/**"
                        ).permitAll()
                        // 그 외 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // Spring Security 기본 로그인 방식 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                // UsernamePasswordAuthenticationFilter 전에 JWT Filter 실행
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}