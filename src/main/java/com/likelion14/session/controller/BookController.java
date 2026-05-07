package com.likelion14.session.controller;

import com.likelion14.session.dto.BookRequestDto;
import com.likelion14.session.dto.BookResponseDto;
import com.likelion14.session.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponseDto create(@RequestBody BookRequestDto request) {
        return bookService.createBook(request);
    }

    @GetMapping
    public List<BookResponseDto> getAll() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public BookResponseDto getOne(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PutMapping("/{id}")
    public BookResponseDto update(@PathVariable Long id, @RequestBody BookRequestDto request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return id + "번 책 삭제 완료!";
    }
}