package com.likelion14.session.auth.common;

import com.likelion14.session.entity.FoodStore;
import com.likelion14.session.exception.FoodStoreNotFoundException;
import com.likelion14.session.repository.FoodStoreRepository;
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
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final FoodStoreRepository foodStoreRepository;

    private final long expirationTime = 1000 * 60 * 60;

    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            FoodStoreRepository foodStoreRepository
    ){
        this.secretKey = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );

        this.foodStoreRepository = foodStoreRepository;
    }

    public String generateToken(FoodStore foodStore){
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(foodStore.getEmail())
                .claim("foodStoreId", foodStore.getId())
                .claim("role", foodStore.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        String email = getEmail(token);

        FoodStore foodStore = foodStoreRepository.findByEmail(email)
                .orElseThrow(FoodStoreNotFoundException::new);

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + foodStore.getRole().name())
        );

        return new UsernamePasswordAuthenticationToken(
                foodStore.getEmail(),
                null,
                authorities
        );
    }
}
