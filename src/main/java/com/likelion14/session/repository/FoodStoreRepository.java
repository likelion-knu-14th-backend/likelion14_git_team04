package com.likelion14.session.repository;

import com.likelion14.session.entity.FoodStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodStoreRepository extends JpaRepository<FoodStore, Long> {
    Optional<FoodStore> findByTel(String tel);

    boolean existsByEmail(String email);

    Optional<FoodStore> findByEmail(String email);
}