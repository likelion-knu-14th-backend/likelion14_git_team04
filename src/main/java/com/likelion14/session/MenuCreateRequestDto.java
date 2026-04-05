package com.likelion14.session;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuCreateRequestDto {

    private String name;
    private int price;
    private String category;


}
