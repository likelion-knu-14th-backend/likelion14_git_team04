package com.likelion14.session.dto;

import com.likelion14.session.entity.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private String title;
    private String isbn;
    private Integer publicationYear;
    private String author;

    private String summary;
    private String publisher;

    public BookResponseDto(Book book) {
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.publicationYear = book.getPublicationYear();
        this.author = book.getAuthor();

        if (book.getBookDetail() != null) {
            this.summary = book.getBookDetail().getSummary();
            this.publisher = book.getBookDetail().getPublisher();
        }
    }

}
