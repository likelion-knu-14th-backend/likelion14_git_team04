package com.likelion14.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RunCreateRequestDto {

    private LocalDateTime startTime; // 시작 시각
    private LocalDateTime endTime; // 종료 시각
    private Double distanceKm; // 러닝 거리 (Km)

}