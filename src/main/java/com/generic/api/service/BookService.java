package com.generic.api.service;

import java.util.stream.Stream;

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

    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ApiException(404, "Livro não encontrado"));
        book.update(request.name(), request.author(), request.available());
        Book saved = bookRepository.save(book);
        return BookMapper.toResponse(saved);
    }

    public void delete(Long id) {
        bookRepository.findById(id).orElseThrow(() -> new ApiException(404, "Livro não encontrado"));
        bookRepository.deleteById(id);
    }

    // TODO: evoluir filtro para um Specification
    public Iterable<BookResponse> getBookAll(String name, String author) {
        Stream<Book> stream = bookRepository.findAll().stream();

        if (name != null) {
            stream = stream.filter(b -> b.getName().toLowerCase().contains(name.toLowerCase()));
        }

        if (author != null) {
            stream = stream.filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()));
        }

        return stream.map(BookMapper::toResponse).toList();
    }

    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ApiException(404, "Livro não encontrado"));
        return BookMapper.toResponse(book);
    }
}
