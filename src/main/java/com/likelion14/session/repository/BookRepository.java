package com.likelion14.session.repository;

import com.likelion14.session.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByMember_Id(Long memberId);
}
