package com.likelion14.session.dto;

import com.likelion14.session.entity.FoodStore;
import lombok.Getter;

@Getter
public class FoodStoreResponseDto {

    private String name;
    private String tel;

    private String category;
    private String address;
    private String description;

    public FoodStoreResponseDto(FoodStore foodStore) {
        this.name = foodStore.getName();
        this.tel = foodStore.getTel();

        this.category = foodStore.getStoreInfo().getCategory();
        this.address = foodStore.getStoreInfo().getAddress();
        this.description = foodStore.getStoreInfo().getDescription();
    }
}