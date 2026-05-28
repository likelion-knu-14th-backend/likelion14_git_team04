package com.likelion14.session.service;

import com.likelion14.session.auth.entity.Member;
import com.likelion14.session.auth.repository.MemberRepository;
import com.likelion14.session.dto.BookRequestDto;
import com.likelion14.session.dto.BookResponseDto;
import com.likelion14.session.entity.Book;
import com.likelion14.session.entity.Category;
import com.likelion14.session.repository.BookRepository;
import com.likelion14.session.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public BookService(
            BookRepository bookRepository,
            CategoryRepository categoryRepository,
            MemberRepository memberRepository
    ) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Novel/Literature"));
            categoryRepository.save(new Category("IT/Programming"));
            categoryRepository.save(new Category("Self Development"));
        }
    }

    @Transactional
    public BookResponseDto createBook(BookRequestDto request) {
        Category category = findCategory(request.getCategoryId());

        Book book = new Book(request.getTitle(), request.getAuthor(), category);
        Book savedBook = bookRepository.save(book);
        return new BookResponseDto(savedBook);
    }

    @Transactional
    public BookResponseDto createMyBook(BookRequestDto request, Long memberId) {
        Category category = findCategory(request.getCategoryId());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        Book book = new Book(request.getTitle(), request.getAuthor(), category, member);
        Book savedBook = bookRepository.save(book);
        return new BookResponseDto(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByMemberId(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        List<Book> books = bookRepository.findAllByMember_Id(memberId);
        List<BookResponseDto> responseList = new ArrayList<>();
        for (Book book : books) {
            responseList.add(new BookResponseDto(book));
        }
        return responseList;
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponseDto> responseList = new ArrayList<>();
        for (Book book : books) {
            responseList.add(new BookResponseDto(book));
        }
        return responseList;
    }

    @Transactional(readOnly = true)
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
        return new BookResponseDto(book);
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));

        Category category = findCategory(request.getCategoryId());

        book.update(request.getTitle(), request.getAuthor(), category);
        return new BookResponseDto(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
        bookRepository.delete(book);
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    }
}
