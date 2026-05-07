package com.likelion14.session.service;

import com.likelion14.session.dto.FoodStoreCreateRequestDto;
import com.likelion14.session.dto.FoodStoreResponseDto;
import com.likelion14.session.entity.StoreInfo;
import com.likelion14.session.entity.FoodStore;
import com.likelion14.session.repository.FoodStoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodStoreService {

    private final FoodStoreRepository foodStoreRepository;

    @Transactional
    public FoodStoreResponseDto createFoodStore(FoodStoreCreateRequestDto request) {
        FoodStore foodStore = new FoodStore(
                request.getName(),
                request.getTel()
        );

        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setCategory(request.getCategory());
        storeInfo.setAddress(request.getAddress());
        storeInfo.setDescription(request.getDescription());
        storeInfo.setFoodStore(foodStore);

        foodStore.setStoreInfo(storeInfo);

        FoodStore savedFoodStore = foodStoreRepository.save(foodStore);
        return new FoodStoreResponseDto(savedFoodStore);
    }

    public List<FoodStoreResponseDto> getFoodStore() {
        return foodStoreRepository.findAll()
                .stream()
                .map(FoodStoreResponseDto::new)
                .toList();
    }

    public FoodStoreResponseDto getFoodStores(String tel) {
        FoodStore foodStore = foodStoreRepository.findByTel(tel)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));

        return new FoodStoreResponseDto(foodStore);
    }

    public FoodStoreResponseDto updateFoodStore(String tel, FoodStoreCreateRequestDto request) {
        FoodStore foodStore = foodStoreRepository.findByTel(tel)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다.."));

        foodStore.update(
                request.getName(),
                request.getTel()
        );

        FoodStore updatedFoodStore = foodStoreRepository.save(foodStore);
        return new FoodStoreResponseDto(updatedFoodStore);
    }

    public void deleteFoodStore(String tel) {
        FoodStore foodStore = foodStoreRepository.findByTel(tel)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));

        foodStoreRepository.delete(foodStore);
    }
}