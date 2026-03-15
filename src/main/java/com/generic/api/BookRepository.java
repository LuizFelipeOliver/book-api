package com.generic.api;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BookRepository
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByName(String name);

    List<Book> findByAvailableTrue();

}
