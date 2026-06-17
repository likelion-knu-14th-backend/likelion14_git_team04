package com.likelion14.session.dto;

import com.likelion14.session.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    private String name;
    private String description;

    public CategoryResponseDto(Category category){
        this.name=category.getName();
        this.description=category.getDescription();
    }
}
