package com.likelion14.session.controller;

import com.likelion14.session.dto.FoodStoreCreateRequestDto;
import com.likelion14.session.dto.FoodStoreResponseDto;
import com.likelion14.session.service.FoodStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodstores")
@RequiredArgsConstructor
public class FoodStoreController {

    private final FoodStoreService foodStoreService;


    @PostMapping
    public FoodStoreResponseDto createFoodStore(@Valid @RequestBody FoodStoreCreateRequestDto request) {
        return foodStoreService.createFoodStore(request);
    }

    @GetMapping
    public List<FoodStoreResponseDto> getFoodStore() {
        return foodStoreService.getFoodStore();
    }

    @GetMapping("/{tel}")
    public FoodStoreResponseDto getFoodStores(@PathVariable String tel) {
        return foodStoreService.getFoodStores(tel);
    }

    @PutMapping("/{tel}")
    public FoodStoreResponseDto updateFoodStore(
            @PathVariable String tel,
            @Valid @RequestBody FoodStoreCreateRequestDto request
    ) {
        return foodStoreService.updateFoodStore(tel, request);
    }

    @DeleteMapping("/{tel}")
    public void deleteFoodStore(@PathVariable String tel) {
        foodStoreService.deleteFoodStore(tel);
    }
}