package com.likelion14.session.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodStoreCreateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다")
    private String name;

    @NotBlank(message = "가게 번호는 필수입니다")
    private String tel;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @NotBlank(message = "주소는 필수입니다")
    private String address;

    private String description;
}