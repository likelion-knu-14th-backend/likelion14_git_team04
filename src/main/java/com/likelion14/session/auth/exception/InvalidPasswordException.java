package com.likelion14.session.auth.exception;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException(String message) {
    super(message);
  }
}
