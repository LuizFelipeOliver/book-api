package com.generic.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.generic.api.request.BookRequest;
import com.generic.api.response.BookResponse;

import jakarta.validation.Valid;

/**
 * BookController
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Iterable<BookResponse> getBookAll() {
        return bookRepository.findAll()
                .stream()
                .map(book -> new BookResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.isAvailable()))
                .toList();
    }

    @GetMapping("/name")
    public List<BookResponse> getByName(@RequestParam("name") String name) {
        return bookRepository.findByName(name)
                .stream()
                .map(book -> new BookResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.isAvailable()))

                .toList();
    }

    // TODO:: adicionar UseCase para desacoplar a lógica do controller
    // TODO: validar unicidade de Book por name e author no UseCase

    @PostMapping
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        Book book = new Book(request.name(), request.author(), request.available());

        Book saved = bookRepository.save(book);

        return new BookResponse(saved.getId(), saved.getName(), saved.getAuthor(), saved.isAvailable());
    }
}
