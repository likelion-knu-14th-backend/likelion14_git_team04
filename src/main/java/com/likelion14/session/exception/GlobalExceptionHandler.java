package com.likelion14.session.exception;

import com.likelion14.session.auth.exception.AlreadyEmailExistsException;
import com.likelion14.session.auth.exception.InvalidPasswordException;
import com.likelion14.session.auth.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AlreadyEmailExistsException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, messageOf(ex, "중복된 요청입니다."), request);
    }

    @ExceptionHandler({
            InvalidPasswordException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, messageOf(ex, "인증에 실패했습니다."), request);
    }

    @ExceptionHandler({
            BookNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, messageOf(ex, "대상을 찾을 수 없습니다."), request);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, messageOf(ex, "잘못된 요청입니다."), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, messageOf(ex, "서버 오류가 발생했습니다."), request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(ErrorResponse.of(status, message, request.getRequestURI()));
    }

    private String messageOf(Exception ex, String fallback) {
        return ex.getMessage() == null || ex.getMessage().isBlank() ? fallback : ex.getMessage();
    }
}
