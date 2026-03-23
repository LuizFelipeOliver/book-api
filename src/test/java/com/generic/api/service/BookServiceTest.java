package com.generic.api.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.generic.api.Book;
import com.generic.api.BookRepository;
import com.generic.api.handler.ApiException;
import com.generic.api.request.BookRequest;
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
    void getBookAll_whenBooksExists_returnsBooksResponse() {
        // Arrage
        Book book = new Book("The Ocean at the End of the Lane", "Neil Gaiman", true);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        // Act
        List<BookResponse> response = (List<BookResponse>) bookService.getBookAll(null, null);

        // Assert
        assertEquals("The Ocean at the End of the Lane", response.get(0).name());
    }

    @Test
    void getBookAll_whenNameFilter_returnsFilteredBooks() {
        // Arrage
        Book book1 = new Book("Between two fires", "Christopher Buehlman", true);
        Book book2 = new Book("Kafka on the Shore", "Haruki Murakami", true);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<BookResponse> response = (List<BookResponse>) bookService.getBookAll("Between two fires", null);

        // Assert
        assertEquals(1, response.size());
        assertEquals("Between two fires", response.get(0).name());
    }

    @Test
    void getBookAll_whenAuthorFilter_returnsFilteredBooks() {
        // Arrage
        Book book1 = new Book("Between two fires", "Christopher Buehlman", true);
        Book book2 = new Book("Kafka on the Shore", "Haruki Murakami", true);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<BookResponse> response = (List<BookResponse>) bookService.getBookAll(null, "Christopher Buehlman");

        // Assert
        assertEquals(1, response.size());
        assertEquals("Christopher Buehlman", response.get(0).author());
    }

    @Test
    void getBookAll_whenBothFilters_returnsFilteredBooks() {
        // Arrage
        Book book1 = new Book("Between two fires", "Christopher Buehlman", true);
        Book book2 = new Book("Kafka on the Shore", "Haruki Murakami", true);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<BookResponse> response = (List<BookResponse>) bookService.getBookAll("Between two fires",
                "Christopher Buehlman");

        // Assert
        assertEquals(1, response.size());
        assertEquals("Christopher Buehlman", response.get(0).author());
        assertEquals("Between two fires", response.get(0).name());
    }

    @Test
    void getBookAll_whenBooksNotExists_rerturnsApiExecption() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of());

        // Assert
        assertTrue(((List<BookResponse>) bookService.getBookAll(null, null)).isEmpty());
    }

    @Test
    void update_WhenBooksExists_returnBookResponse() {
        // Arrage
        Book book = new Book("Between two fires", "Christopher Buehlman", true);
        Book updatedBook = new Book("New Name", "New Author", false);
        BookRequest request = new BookRequest("New Name", "New Author", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(updatedBook);

        // Act
        BookResponse response = bookService.update(1L, request);

        // Assert
        assertEquals("New Name", response.name());
        assertEquals("New Author", response.author());
    }

    @Test
    void update_whenBookNotExists_throwsApiException() {
        // Arrange
        BookRequest request = new BookRequest("New Name", "New Author", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ApiException.class, () -> bookService.update(1L, request));
    }

    @Test
    void delete_whenBookExists_deletesSuccessfully() {
        // Arrange
        Book book = new Book("Between two fires", "Christopher Buehlman", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        bookService.delete(1L);

        // Assert
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void delete_whenBookNotExists_throwsApiException() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ApiException.class, () -> bookService.delete(1L));
    }

}
