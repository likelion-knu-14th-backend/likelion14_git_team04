package com.likelion14.session.service;

import com.likelion14.session.dto.StudentCreateRequestDto;
import com.likelion14.session.dto.StudentResponseDto;
import com.likelion14.session.entity.Profile;
import com.likelion14.session.entity.Student;
import com.likelion14.session.exception.StudentNotFoundException;
import com.likelion14.session.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentResponseDto createStudent(StudentCreateRequestDto request) {
        Student student = new Student(
                request.getName(),
                request.getStudentNumber(),
                request.getAge(),
                request.getMajor()
        );

        Profile profile = new Profile();
        profile.setBio(request.getBio());
        profile.setPhoneNum(request.getPhoneNum());
        profile.setStudent(student);

        student.setProfile(profile);

        Student savedStudent = studentRepository.save(student);
        return new StudentResponseDto(savedStudent);
    }

    public List<StudentResponseDto> getStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponseDto::new)
                .toList();
    }

    public StudentResponseDto getStudent(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(StudentNotFoundException::new);

        return new StudentResponseDto(student);
    }

    @Transactional
    public StudentResponseDto updateStudent(String studentNumber, StudentCreateRequestDto request) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(StudentNotFoundException::new);

        student.update(
                request.getName(),
                request.getStudentNumber(),
                request.getAge(),
                request.getMajor()
        );

        Profile profile = new Profile();
        profile.setBio(request.getBio());
        profile.setPhoneNum(request.getPhoneNum());
        profile.setStudent(student);

        student.setProfile(profile);

        Student updatedStudent = studentRepository.save(student);
        return new StudentResponseDto(updatedStudent);
    }

    public void deleteStudent(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(StudentNotFoundException::new);

        studentRepository.delete(student);
    }
}