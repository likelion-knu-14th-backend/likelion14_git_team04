package com.likelion14.session.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentCreateRequestDto {

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "학번은 필수입니다")
    private String studentNumber;

    @NotNull(message = "나이는 필수입니다")
    @Positive(message = "나이는 양수여야 합니다")
    private Integer age;

    @NotBlank(message = "전공은 필수입니다")
    private String major;

    private String bio;
    private String phoneNum;
}