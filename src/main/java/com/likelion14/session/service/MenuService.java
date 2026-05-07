package com.likelion14.session.service;

import com.likelion14.session.dto.MenuRequestDto;
import com.likelion14.session.dto.MenuResponseDto;
import com.likelion14.session.entity.Menu;
import com.likelion14.session.entity.FoodStore;
import com.likelion14.session.repository.MenuRepository;
import com.likelion14.session.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final FoodStoreRepository foodStoreRepository;
    private final MenuRepository menuRepository;

    public void uploadFoodStoreMenu(
            String tel, List<MenuRequestDto> gradeRequestDtoList) {
        FoodStore foodStore = foodStoreRepository.findByTel(tel)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));

        List<Menu> menuList = gradeRequestDtoList.stream()
                .map(dto -> {
                    Menu menu = new Menu();
                    menu.setMenuName(dto.getMenuName());
                    menu.setPrice(dto.getPrice());
                    menu.setStock(dto.getStock());
                    menu.setFoodStore(foodStore);
                    return menu;
                }).toList();

        menuRepository.saveAll(menuList);
    }

    public List<MenuResponseDto> getFoodStoreMenu(String tel) {
        FoodStore foodStore = foodStoreRepository.findByTel(tel)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));

        List<Menu> menuList = menuRepository.findAllByFoodStore(foodStore);

        return menuList.stream()
                .map(MenuResponseDto::new)
                .toList();
    }
}