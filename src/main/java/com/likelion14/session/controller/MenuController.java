package com.likelion14.session.controller;

import com.likelion14.session.dto.MenuRequestDto;
import com.likelion14.session.dto.MenuResponseDto;
import com.likelion14.session.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/{tel}")
    public void uploadFoodStoreMenu(
            @PathVariable("tel") String tel,
            @RequestBody List<MenuRequestDto> gradeRequestDtoList
    ) {
        menuService.uploadFoodStoreMenu(tel, gradeRequestDtoList);
    }

    @GetMapping("/{tel}")
    public List<MenuResponseDto> getFoodStoreMenu(
            @PathVariable("tel") String tel) {
        return menuService.getFoodStoreMenu(tel);
    }
}