package com.likelion14.session.service;

import com.likelion14.session.dto.BookCreateRequestDto;
import com.likelion14.session.dto.BookResponseDto;
import com.likelion14.session.entity.BookDetail;
import com.likelion14.session.entity.Book;
import com.likelion14.session.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookResponseDto createBook(BookCreateRequestDto request) {
        Book book = new Book(
                request.getTitle(),
                request.getIsbn(),
                request.getPublicationYear(),
                request.getAuthor()
        );

        BookDetail bookDetail = new BookDetail();
        bookDetail.setSummary(request.getSummary());
        bookDetail.setPublisher(request.getPublisher());
        bookDetail.setBook(book);

        book.setBookDetail(bookDetail);

        Book savedBook = bookRepository.save(book);
        return new BookResponseDto(savedBook);
    }

    public List<BookResponseDto> getBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponseDto::new)
                .toList();
    }

    public BookResponseDto getBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        return new BookResponseDto(book);
    }

    @Transactional
    public BookResponseDto updateBook(String isbn, BookCreateRequestDto request) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        book.update(
                request.getTitle(),
                request.getIsbn(),
                request.getPublicationYear(),
                request.getAuthor()
        );
        
        if (book.getBookDetail() != null) {
            book.getBookDetail().setSummary(request.getSummary());
            book.getBookDetail().setPublisher(request.getPublisher());
        }

        Book updatedBook = bookRepository.save(book);
        return new BookResponseDto(updatedBook);
    }

    @Transactional
    public void deleteBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        bookRepository.delete(book);
    }
}
