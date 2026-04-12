package com.likelion14.session.controller;

import com.likelion14.session.dto.RunCreateRequestDto;
import com.likelion14.session.dto.RunResponseDto;
import com.likelion14.session.service.RunService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/run")
@RequiredArgsConstructor
public class RunController {

    private final RunService runService;

    // 러닝 등록
    @PostMapping
    public RunResponseDto createRun(@RequestBody RunCreateRequestDto request) {
        return runService.createRun(request);
    }

    // 전체 러닝 조회
    @GetMapping
    public List<RunResponseDto> getRuns() {
        return runService.getRuns();
    }

    // ID 기준 단건 조회
    @GetMapping("/{id}")
    public RunResponseDto getRun(@PathVariable String id) {
        return runService.getRun(id);
    }

    // ID 기준 수정
    @PutMapping("/{id}")
    public RunResponseDto updateStudent(
            @PathVariable String id,
            @RequestBody RunCreateRequestDto request
    ) {
        return runService.updateRun(id, request);
    }

    // ID 기준 삭제
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        runService.deleteRun(id);
    }
}