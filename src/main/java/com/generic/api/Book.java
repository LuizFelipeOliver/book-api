package com.generic.api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Book
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "[Book]Name is required.")
    @Size(min = 3)
    private String name;

    @NotBlank(message = "[Book]Author is required.")
    @Size(min = 3)
    private String author;

    @NotNull
    private Boolean available = false;

    protected Book() {
    }

    public Book(String name, String author, Boolean available) {
        this.name = name;
        this.author = author;
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format(
                "Book[id=%d, name='%s', author='%s', available='%s']",
                id, name, author, available);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean isAvailable() {
        return available;
    }

}
