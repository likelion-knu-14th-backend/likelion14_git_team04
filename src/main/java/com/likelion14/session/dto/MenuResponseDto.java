package com.likelion14.session.dto;

import com.likelion14.session.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponseDto {
    private String menuName;
    private int price;
    private int stock;

    public MenuResponseDto(Menu menu){
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
        this.stock = menu.getStock();
    }

}
