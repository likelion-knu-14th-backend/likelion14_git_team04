package com.likelion14.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodStoreCreateRequestDto {

    private String name;
    private String tel;

    private String category;
    private String address;
    private String description;
}