package com.generic.api.response;

/**
 * BookResponse
 */
public record BookResponse(
        Long id,
        String name,
        String author,
        Boolean available,
        String message) {
    public BookResponse(Long id, String name, String author, Boolean available) {
        this(id, name, author, available, "Livro Salvo! 📚");
    }
}
