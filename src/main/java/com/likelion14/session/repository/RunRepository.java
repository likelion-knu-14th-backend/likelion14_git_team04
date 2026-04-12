package com.likelion14.session.repository;

import com.likelion14.session.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RunRepository extends JpaRepository<Run, Long> {
    Optional<Run> findById(String studentNumber);
}