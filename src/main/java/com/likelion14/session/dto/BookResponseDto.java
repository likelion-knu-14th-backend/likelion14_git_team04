package com.likelion14.session.dto;

import com.likelion14.session.entity.Book;

public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String categoryName;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.categoryName = book.getCategory().getName();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategoryName() { return categoryName; }
}