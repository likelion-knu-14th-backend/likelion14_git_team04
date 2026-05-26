package com.likelion14.session.auth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.dto.LoginRequestDto;
import com.likelion14.session.auth.dto.SignupRequestDto;
import com.likelion14.session.auth.dto.TokenResponseDto;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.exception.AlreadyEmailExistException;
import com.likelion14.session.auth.exception.InvalidPasswordException;
import com.likelion14.session.entity.FoodStore;
import com.likelion14.session.exception.FoodStoreNotFoundException;
import com.likelion14.session.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FoodStoreRepository foodStoreRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signup(SignupRequestDto request){
        if(foodStoreRepository.existsByEmail(request.getEmail())){
            throw new AlreadyEmailExistException();
        }
        FoodStore foodStore = new FoodStore();
        foodStore.setEmail(request.getEmail());

        foodStore.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        foodStore.setName(request.getName());
        foodStore.setTel(request.getTel());

        foodStore.setRole(Role.OWNER);

        foodStoreRepository.save(foodStore);
    }

    public TokenResponseDto login(LoginRequestDto request){
        FoodStore foodStore = foodStoreRepository.findByEmail(request.getEmail())
                .orElseThrow(FoodStoreNotFoundException::new);

        if(!passwordEncoder.matches(
                request.getPassword(),
                foodStore.getPassword()
        )){
            throw new InvalidPasswordException();
        }

        String accessToken = jwtTokenProvider.generateToken(foodStore);
        return new TokenResponseDto(foodStore.getName(), accessToken);

    }
}
