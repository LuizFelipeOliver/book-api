package com.generic.api;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * BookRepository
 */
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByName(String name);

    List<Book> findByAvailableTrue();

}
