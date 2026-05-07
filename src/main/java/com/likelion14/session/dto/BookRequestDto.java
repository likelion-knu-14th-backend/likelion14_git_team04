package com.likelion14.session.dto;

public class BookRequestDto {
    private String title;
    private String author;
    private Long categoryId;

    public BookRequestDto() {}

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Long getCategoryId() { return categoryId; }
}