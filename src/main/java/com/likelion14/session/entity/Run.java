package com.likelion14.session.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Double distanceKm;

    public Run(LocalDateTime startTime, LocalDateTime endTime, Double distanceKm) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distanceKm = distanceKm;
    }

    public void update(LocalDateTime startTime, LocalDateTime endTime, Double distanceKm) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distanceKm = distanceKm;
    }
}