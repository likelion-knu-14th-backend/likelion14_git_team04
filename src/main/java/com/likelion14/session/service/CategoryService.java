package com.likelion14.session.service;

import com.likelion14.session.dto.CategoryRequestDto;
import com.likelion14.session.dto.CategoryResponseDto;
import com.likelion14.session.entity.Category;
import com.likelion14.session.entity.Book;
import com.likelion14.session.repository.CategoryRepository;
import com.likelion14.session.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void uploadBookCategory(
            String isbn, List<CategoryRequestDto> categoryRequestDtoList) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        List<Category> categoryList = categoryRequestDtoList.stream()
                .map(dto -> {
                    Category category = new Category();
                    category.setName(dto.getName());
                    category.setDescription(dto.getDescription());
                    category.setBook(book);
                    return category;
                }).toList();

        categoryRepository.saveAll(categoryList);
    }

    public List<CategoryResponseDto> getBookCategory(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        List<Category> categoryList = categoryRepository.findAllByBook(book);

        return categoryList.stream()
                .map(CategoryResponseDto::new)
                .toList();
    }
}
