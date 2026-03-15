package com.generic.api.mapper;

import com.generic.api.Book;

import com.generic.api.request.BookRequest;
import com.generic.api.response.BookResponse;

/**
 * BookMapper
 */
public class BookMapper {

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.isAvailable());
    }

    public static Book toEntity(BookRequest request) {
        return new Book(
                request.name(),
                request.author(),
                request.available());
    }
}
