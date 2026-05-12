package com.likelion14.session.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRequestDto {
    @NotBlank(message = "메뉴 이름은 필수입니다")
    private String menuName;

    @NotNull(message = "가격은 필수입니다")
    private int price;

    private int stock;
}
