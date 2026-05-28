package com.likelion14.session.entity;

import com.likelion14.session.auth.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Book() {
    }

    public Book(String title, String author, Category category) {
        this(title, author, category, null);
    }

    public Book(String title, String author, Category category, Member member) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.member = member;
    }

    public void update(String title, String author, Category category) {
        this.title = title;
        this.author = author;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public Member getMember() {
        return member;
    }
}
