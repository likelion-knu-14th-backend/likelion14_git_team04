package com.likelion14.session;

import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/run")
public class RunController {

    private final List<RunResponseDto> runStore = new ArrayList<>();

    @PostMapping
    public RunResponseDto createRun(@RequestBody RunCreateRequestDto request){
        //===== 사용자용 러닝 데이터 가공 =====
        LocalDateTime startTimeRequest = request.getStartTime();
        LocalDateTime endTimeRequest = request.getEndTime();
        long runSec = Duration.between(startTimeRequest, endTimeRequest).toSeconds();

        String runDate = String.format("%d-%02d-%02d",
                startTimeRequest.getYear(),
                startTimeRequest.getMonthValue(),
                startTimeRequest.getDayOfMonth()
        );

        String startTime = String.format("%02d:%02d",
                startTimeRequest.getHour(),
                startTimeRequest.getMinute()
        );

        String endTime = String.format("%02d:%02d",
                endTimeRequest.getHour(),
                endTimeRequest.getMinute()
        );

        String totalTime = String.format("%02d:%02d",
                runSec / 60,
                runSec % 60
        );

        Double distanceKm = request.getDistanceKm();

        double paceTotalSec = runSec / distanceKm;
        int paceMin = (int) (paceTotalSec / 60);
        int paceSec = (int) (paceTotalSec % 60);
        String avgPace = String.format("%d'%02d\"",
                paceMin,
                paceSec
        );

        //===== response 객체 생성 =====
        RunResponseDto run = new RunResponseDto(
                runDate,
                startTime,
                endTime,
                totalTime,
                distanceKm,
                avgPace
        );

        runStore.add(run);
        return  run;
    }

    //===== RunDate 기준으로 조회 =====
    @GetMapping("/{runDate}") //YYYY-MM-DD
    public RunResponseDto getRun(@PathVariable String runDate){
        for(RunResponseDto run : runStore){
            if(run.getRunDate().equals(runDate)){
                return run;
            }
        }
        return null;
    }

    @PutMapping("/{runDate}") //YYYY-MM-DD
    public RunResponseDto updateRun(
            @PathVariable String runDate,
            @RequestBody RunResponseDto request
    ){
        for(int i = 0; i < runStore.size(); i++){
            RunResponseDto run = runStore.get(i);

            if(run.getRunDate().equals(runDate)){
                RunResponseDto updateRun = new RunResponseDto(
                        request.getRunDate(),
                        request.getStartTime(),
                        request.getEndTime(),
                        request.getTotalTime(),
                        request.getDistanceKm(),
                        request.getAvgPace()
                );
                runStore.set(i, updateRun);
                return updateRun;
            }
        }
        return null;
    }

    @DeleteMapping("/{runDate}") //YYYY-MM-DD
    public void deleteRun(@PathVariable String runDate){
        for(int i = 0; i< runStore.size(); i++){
            RunResponseDto run = runStore.get(i);

            if(run.getRunDate().equals(runDate)){
                runStore.remove(i);
            }
        }
    }
}
