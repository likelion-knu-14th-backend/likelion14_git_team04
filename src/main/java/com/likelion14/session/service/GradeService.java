package com.likelion14.session.service;

import com.likelion14.session.dto.GradeRequestDto;
import com.likelion14.session.dto.GradeResponseDto;
import com.likelion14.session.entity.Grade;
import com.likelion14.session.entity.Student;
import com.likelion14.session.repository.GradeRepository;
import com.likelion14.session.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;

    public void uploadStudentGrade(
            String studentNumber, List<GradeRequestDto> gradeRequestDtoList) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다."));

        List<Grade> gradeList = gradeRequestDtoList.stream()
                .map(dto -> {
                    Grade grade = new Grade();
                    grade.setSubjectName(dto.getSubjectName());
                    grade.setGrade(dto.getGrade());
                    grade.setStudent(student);
                    return grade;
                }).toList();

        gradeRepository.saveAll(gradeList);
    }

    public List<GradeResponseDto> getStudentGrade(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다."));

        List<Grade> gradeList = gradeRepository.findAllByStudent(student);

        return gradeList.stream()
                .map(GradeResponseDto::new)
                .toList();
    }
}