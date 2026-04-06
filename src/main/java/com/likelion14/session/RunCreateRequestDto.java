package com.likelion14.session;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

// ===== 사용자가 입력한 원본 데이터 =====
public class RunCreateRequestDto {
    private LocalDateTime startTime; // 시작 시각
    private LocalDateTime endTime; // 종료 시각
    private Double distanceKm; // 러닝 거리 (Km)
}

/* JSON 요청 예시
{
    "startTime" : "2026-04-06T08:13:03",
    "endTime" : "2026-04-06T10:08:21",
    "distanceKm" : 21.1
}
 */
