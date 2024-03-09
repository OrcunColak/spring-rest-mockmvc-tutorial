package com.colak.springrestmockmvctutorial.service;

import com.colak.springrestmockmvctutorial.model.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

@Slf4j
public class BookService {

    private final List<Book> bookList = new ArrayList<>();

    public Book create(Book book) {
        bookList.add(book);
        return book;
    }

    public Optional<Book> getById(Long bookId) {
        return bookList.stream()
                .filter(book -> book.getId() == bookId)
                .findFirst();
    }

    public boolean update(Book book) {
        boolean result = false;
        Optional<Book> existingBook = getById(book.getId());

        if (existingBook.isPresent()) {
            int index = bookList.indexOf(existingBook.get());
            bookList.set(index, book);
            result = true; // Book updated successfully
        }
        return result; // Book with the given ID not found
    }

    public boolean delete(Long bookId) {
        return bookList.removeIf(book -> book.getId() == bookId);
    }
}
