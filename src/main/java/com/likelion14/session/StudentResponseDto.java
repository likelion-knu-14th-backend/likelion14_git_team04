package com.likelion14.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponseDto {

    private String name;
    private String studentNumber;
    private Integer age;
    private String major;
}
