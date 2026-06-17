package com.likelion14.session.auth.exception;

public class AlreadyEmailExistsException extends RuntimeException {
    public AlreadyEmailExistsException(String message) {
        super(message);
    }
}
