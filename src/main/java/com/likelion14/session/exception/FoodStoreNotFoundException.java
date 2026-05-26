package com.likelion14.session.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/* [RuntimeException -> UsernameNotFoundException 변경]
RuntimeException은 Spring Security가 예외를 가로채서 403을 보냄
UsernameNotFoundException을 상속받으면,
GlobalExceptionHandler의 404가 정상 작동함 */

public class FoodStoreNotFoundException extends UsernameNotFoundException {
    public FoodStoreNotFoundException(){
        super("해당 식당이 존재하지 않습니다");
    }
}

