package com.likelion14.session.service;

import com.likelion14.session.dto.RunCreateRequestDto;
import com.likelion14.session.dto.RunResponseDto;
import com.likelion14.session.entity.Run;
import com.likelion14.session.repository.RunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RunService {

    private final RunRepository runRepository;

    public RunResponseDto createRun(RunCreateRequestDto request) {
        Run run = new Run(
                request.getStartTime(),
                request.getEndTime(),
                request.getDistanceKm()
        );

        Run savedRun = runRepository.save(run);
        return new RunResponseDto(savedRun);
    }

    public List<RunResponseDto> getRuns() {
        return runRepository.findAll()
                .stream()
                .map(RunResponseDto::new)
                .toList();
    }

    public RunResponseDto getRun(String studentNumber) {
        Run run = runRepository.findById(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다."));

        return new RunResponseDto(run);
    }

    public RunResponseDto updateRun(String studentNumber, RunCreateRequestDto request) {
        Run run = runRepository.findById(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다."));

        run.update(
                request.getStartTime(),
                request.getEndTime(),
                request.getDistanceKm()
        );

        Run updatedRun = runRepository.save(run);
        return new RunResponseDto(updatedRun);
    }

    public void deleteRun(String studentNumber) {
        Run run = runRepository.findById(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다."));

        runRepository.delete(run);
    }
}