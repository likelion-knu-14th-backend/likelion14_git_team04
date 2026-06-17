package com.likelion14.session.controller;

import com.likelion14.session.dto.BookCreateRequestDto;
import com.likelion14.session.dto.BookResponseDto;
import com.likelion14.session.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 책 등록
    @PostMapping
    public BookResponseDto createBook(@RequestBody BookCreateRequestDto request) {
        return bookService.createBook(request);
    }

    // 전체 책 조회
    @GetMapping
    public List<BookResponseDto> getBooks() {
        return bookService.getBooks();
    }

    // ISBN 기준 단건 조회
    @GetMapping("/{isbn}")
    public BookResponseDto getBook(@PathVariable String isbn) {
        return bookService.getBook(isbn);
    }

    // ISBN 기준 수정
    @PutMapping("/{isbn}")
    public BookResponseDto updateBook(
            @PathVariable String isbn,
            @RequestBody BookCreateRequestDto request
    ) {
        return bookService.updateBook(isbn, request);
    }

    // ISBN 기준 삭제
    @DeleteMapping("/{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }
}
