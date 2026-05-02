package com.likelion14.session.repository;

import com.likelion14.session.entity.Grade;
import com.likelion14.session.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findAllByStudent(Student student);
}
