package com.likelion14.session.dto;

import com.likelion14.session.entity.Run;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class RunResponseDto {

    private String runDate; // 러닝 일자
    private String startTime; // 시작 시간
    private String endTime; // 종료 시간
    private String totalTime; // 총 시간
    private Double distanceKm; // 총 거리
    private String avgPace; // 평균 페이스

    public RunResponseDto(Run run) {
        LocalDateTime startTimeRequest = run.getStartTime();
        LocalDateTime endTimeRequest = run.getEndTime();
        long runSec = Duration.between(startTimeRequest, endTimeRequest).toSeconds();

        // 러닝 일자
        this.runDate = startTimeRequest.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 시작 시간
        this.startTime = startTimeRequest.format(DateTimeFormatter.ofPattern("HH:mm"));

        // 종료 시간
        this.endTime = endTimeRequest.format(DateTimeFormatter.ofPattern("HH:mm"));

        // 총 시간
        this.totalTime = String.format("%02d:%02d",
                runSec / 60,
                runSec % 60
        );

        // 총 거리
        this.distanceKm = run.getDistanceKm();

        // 평균 페이스
        double paceTotalSec;
        if(distanceKm > 0) {
            paceTotalSec = runSec / distanceKm;
            int paceMin = (int) (paceTotalSec / 60);
            int paceSec = (int) (paceTotalSec % 60);
            this.avgPace = String.format("%d'%02d\"",
                    paceMin,
                    paceSec
            );
        }
        else
            this.avgPace = "0'00\"";
    }
}