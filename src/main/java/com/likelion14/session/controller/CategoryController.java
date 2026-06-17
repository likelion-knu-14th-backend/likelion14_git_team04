package com.likelion14.session.controller;

import com.likelion14.session.dto.CategoryRequestDto;
import com.likelion14.session.dto.CategoryResponseDto;
import com.likelion14.session.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/{isbn}")
    public void uploadBookCategory(
            @PathVariable("isbn") String isbn,
            @RequestBody List<CategoryRequestDto> categoryRequestDtoList
    ) {
        categoryService.uploadBookCategory(isbn, categoryRequestDtoList);
    }

    @GetMapping("/{isbn}")
    public List<CategoryResponseDto> getBookCategory(
            @PathVariable("isbn") String isbn) {
        return categoryService.getBookCategory(isbn);
    }
}
