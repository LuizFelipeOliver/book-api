package com.generic.api.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.generic.api.Book;
import com.generic.api.BookRepository;
import com.generic.api.handler.ApiException;
import com.generic.api.response.BookResponse;

/**
 * BookServiceTest
 */
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_whenBookExists_returnsBookResponse() {
        // Arrange
        Book book = new Book("Mistborn", "Brandon Sanderson", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        BookResponse response = bookService.getById(1L);

        // Assert
        assertEquals("Mistborn", response.name());
    }

    @Test
    void getById_whenBookNotExists_returnsApiExecption() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ApiException.class, () -> bookService.getById(1L));
    }

    @Test
    void getBookAll_whenBooksExistis_returnsBooksResponse() {
        // Arrage
        Book book = new Book("The Ocean at the End of the Lane", "Neil Gaiman", true);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        // Act
        List<BookResponse> response = (List<BookResponse>) bookService.getBookAll();

        // Assert
        assertEquals("The Ocean at the End of the Lane", response.get(0).name());
    }

    @Test
    void getBookAll_whenBooksNotExists_rerturnsApiExecption() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of());

        // Assert
        assertTrue(((List<BookResponse>) bookService.getBookAll()).isEmpty());
    }

}
