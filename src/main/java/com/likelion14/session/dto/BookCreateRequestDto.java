package com.likelion14.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookCreateRequestDto {

    private String title;
    private String isbn;
    private Integer publicationYear;
    private String author;

    private String summary;
    private String publisher;
}
