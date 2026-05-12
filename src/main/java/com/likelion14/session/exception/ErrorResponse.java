package com.likelion14.session.exception;

public record ErrorResponse(
        String code,
        String message
) {
}