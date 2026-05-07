package com.likelion14.session.repository;

import com.likelion14.session.entity.Menu;
import com.likelion14.session.entity.FoodStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByFoodStore(FoodStore foodStore);
}
