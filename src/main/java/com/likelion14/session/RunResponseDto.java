package com.likelion14.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

// ===== 시간 및 페이스 가공 후 데이터 =====
public class RunResponseDto {
    private String runDate;
    private String startTime;
    private String endTime;
    private String totalTime;
    private Double distanceKm;
    private String avgPace;
}

/* JSON 응답 예시
{
    "runDate": "2026-04-06",
    "startTime": "08:13",
    "endTime": "10:08",
    "totalTime": "115:18",
    "distanceKm": 21.1,
    "avgPace": "5'27\""
}
 */
