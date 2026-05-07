package com.likelion14.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FoodStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String tel;

    public FoodStore(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public void update(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }


    @OneToOne(mappedBy = "foodStore", cascade = CascadeType.ALL)
    private StoreInfo storeInfo;
}