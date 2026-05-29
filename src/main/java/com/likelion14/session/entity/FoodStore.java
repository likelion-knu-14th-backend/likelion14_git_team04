package com.likelion14.session.entity;

import com.likelion14.session.auth.enums.Role;
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

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;
    private String providerId;


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