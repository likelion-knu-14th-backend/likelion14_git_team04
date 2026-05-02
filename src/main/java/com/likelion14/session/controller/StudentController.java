package com.likelion14.session.controller;

import com.likelion14.session.dto.StudentCreateRequestDto;
import com.likelion14.session.dto.StudentResponseDto;
import com.likelion14.session.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // 학생 등록
    @PostMapping
    public StudentResponseDto createStudent(@RequestBody StudentCreateRequestDto request) {
        return studentService.createStudent(request);
    }

    // 전체 학생 조회
    @GetMapping
    public List<StudentResponseDto> getStudents() {
        return studentService.getStudents();
    }

    // 학번 기준 단건 조회
    @GetMapping("/{studentNumber}")
    public StudentResponseDto getStudent(@PathVariable String studentNumber) {
        return studentService.getStudent(studentNumber);
    }

    // 학번 기준 수정
    @PutMapping("/{studentNumber}")
    public StudentResponseDto updateStudent(
            @PathVariable String studentNumber,
            @RequestBody StudentCreateRequestDto request
    ) {
        return studentService.updateStudent(studentNumber, request);
    }

    // 학번 기준 삭제git checkout juyoung-session3
    @DeleteMapping("/{studentNumber}")
    public void deleteStudent(@PathVariable String studentNumber) {
        studentService.deleteStudent(studentNumber);
    }
}