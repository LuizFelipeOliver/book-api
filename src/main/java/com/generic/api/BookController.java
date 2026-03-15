package com.generic.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generic.api.request.BookRequest;
import com.generic.api.response.BookResponse;
import com.generic.api.service.BookService;

import jakarta.validation.Valid;

/**
 * BookController
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // TODO: futurametne fazer filtragrem nos metodos get para filtrar por autor
    @GetMapping
    public Iterable<BookResponse> getBookAll() {
        return bookService.getBookAll();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    // TODO: futurametne fazer filtragrem nos metodos get para filtrar por autor
    @GetMapping("/name")
    public List<BookResponse> getByName(@RequestParam("name") String name) {
        return bookService.getByName(name);
    }

    // TODO: validar unicidade de Book por name e author no UseCase
    @PostMapping
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return bookService.create(request);
    }
}
