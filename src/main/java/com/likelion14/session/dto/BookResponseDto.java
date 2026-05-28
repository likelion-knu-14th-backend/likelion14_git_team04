package com.likelion14.session.dto;

import com.likelion14.session.entity.Book;

public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String categoryName;
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.categoryName = book.getCategory().getName();
        if (book.getMember() != null) {
            this.ownerId = book.getMember().getId();
            this.ownerName = book.getMember().getName();
            this.ownerEmail = book.getMember().getEmail();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
}
