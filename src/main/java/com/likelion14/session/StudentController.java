package com.likelion14.session;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<StudentResponseDto> studentStore = new ArrayList<>();

    //학생등록 POST
    @PostMapping
    public StudentResponseDto createStudent(@RequestBody StudentCreateRequestDto request) {
        StudentResponseDto student = new StudentResponseDto(
                request.getName(),
                request.getStudentNumber(),
                request.getAge(),
                request.getMajor()
        );

        studentStore.add(student);
        return student;

    }

    //학생조회
    @GetMapping
    public List<StudentResponseDto> getStudents() {
        return studentStore;
    }

    //Get - 단건 조회
    @GetMapping("/{studentNumber}")
    public StudentResponseDto getStudent(@PathVariable String studentNumber) {
        for (StudentResponseDto student : studentStore) {
            if (student.getStudentNumber().equals(studentNumber)) {
                return student;
            }
        }
        return null;
    }

    // PUT - 학번 기준 수정
    @PutMapping("/{studentNumber}")
    public StudentResponseDto updateStudent(
            @PathVariable String studentNumber,
            @RequestBody StudentCreateRequestDto request
    ) {
        for (int i = 0; i < studentStore.size(); i++) {
            StudentResponseDto student = studentStore.get(i);

            if (student.getStudentNumber().equals(studentNumber)) {
                StudentResponseDto updateStudent = new StudentResponseDto(
                        request.getName(),
                        request.getStudentNumber(),
                        request.getAge(),
                        request.getMajor()
                );

                studentStore.set(i, updateStudent);
                return updateStudent;
            }
        }
        return null;
    }

    // DELETE - 학번 기준 삭제
    @DeleteMapping("/{studentNumber}")
    public void deleteStudent(@PathVariable String studentNumber) {
        for (int i = 0; i < studentStore.size(); i++) {
            StudentResponseDto student = studentStore.get(i);

            if (student.getStudentNumber().equals(studentNumber)) {
                studentStore.remove(i);
                return;
            }
        }
    }
}
