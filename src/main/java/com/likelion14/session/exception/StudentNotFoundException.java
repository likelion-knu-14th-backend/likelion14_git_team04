package com.likelion14.session.exception;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(){
        super("해당 학생이 존재하지 않습니다");
    }
}
