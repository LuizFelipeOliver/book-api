package com.generic.api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Book
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String author;
    private Boolean available = false;

    protected Book() {
    }

    public Book(String name, String author, Boolean available) {
        this.name = name;
        this.author = author;
        this.available = available != null ? available : false;
    }

    public void update(String name, String author, Boolean available) {
        this.name = name;
        this.author = author;
        this.available = available != null ? available : false;
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
