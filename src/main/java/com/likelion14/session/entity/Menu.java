package com.likelion14.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;
    private int price;
    private int stock;

    @ManyToOne
    @JoinColumn(name="foodstore_id")
    private FoodStore foodStore;
}
