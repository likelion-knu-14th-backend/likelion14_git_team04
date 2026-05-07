package com.likelion14.session.entity;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Category() {}
    public Category(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
}