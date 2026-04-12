package com.likelion14.session.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String studentNumber;

    private Integer age;

    private String major;

    public Student(String name, String studentNumber, Integer age, String major) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.age = age;
        this.major = major;
    }

    public void update(String name, String studentNumber, Integer age, String major) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.age = age;
        this.major = major;
    }
}