package com.generic.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generic.api.Book;
import com.generic.api.BookRepository;
import com.generic.api.request.BookRequest;
import com.generic.api.response.BookResponse;
import com.generic.api.handler.ApiException;
import com.generic.api.mapper.BookMapper;

/**
 * BookService
 * TODO: estudar como o Spring gerencia dependências por baixo(IoC
 * Container/Dependency Injection)
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse create(BookRequest request) {
        Book book = BookMapper.toEntity(request);

        Book saved = bookRepository.save(book);

        return BookMapper.toResponse(saved);
    }

    public Iterable<BookResponse> getBookAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ApiException(404, "Livro não encontrado"));
        return BookMapper.toResponse(book);
    }

    public List<BookResponse> getByName(String name) {
        return bookRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }
}
