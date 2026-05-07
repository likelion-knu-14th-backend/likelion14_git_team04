package com.likelion14.session.service;

import com.likelion14.session.dto.BookRequestDto;
import com.likelion14.session.dto.BookResponseDto;
import com.likelion14.session.entity.Book;
import com.likelion14.session.entity.Category;
import com.likelion14.session.repository.BookRepository;
import com.likelion14.session.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    // 카테고리(1 소설 2 it 3 자기계발)
    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("소설/문학"));
            categoryRepository.save(new Category("IT/프로그래밍"));
            categoryRepository.save(new Category("자기계발"));
        }
    }

    @Transactional
    public BookResponseDto createBook(BookRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("없는 카테고리입니다."));

        Book book = new Book(request.getTitle(), request.getAuthor(), category);
        Book savedBook = bookRepository.save(book);
        return new BookResponseDto(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponseDto> responseList = new ArrayList<>();
        for (Book book : books) {
            responseList.add(new BookResponseDto(book));
        }
        return responseList;
    }

    @Transactional(readOnly = true)
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 책입니다."));
        return new BookResponseDto(book);
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 책입니다."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("없는 카테고리입니다."));

        book.update(request.getTitle(), request.getAuthor(), category);
        return new BookResponseDto(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 책입니다."));
        bookRepository.delete(book);
    }
}