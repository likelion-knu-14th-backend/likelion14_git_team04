package com.likelion14.session.repository;

import com.likelion14.session.entity.Book;
import com.likelion14.session.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByBook(Book book);
}
