package com.likelion14.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class StoreInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String address;
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodstore_id")
    private FoodStore foodStore;
}