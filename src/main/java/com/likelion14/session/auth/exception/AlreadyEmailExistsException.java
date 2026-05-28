package com.likelion14.session.auth.exception;

public class AlreadyEmailExistException extends RuntimeException {
    public AlreadyEmailExistException(String message) {

        super("이미 존재하는 이메일입니다.");
    }
}
