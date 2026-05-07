package com.likelion14.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRequestDto {
    private String menuName;
    private int price;
    private int stock;
}
