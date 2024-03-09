package com.colak.springrestmockmvctutorial.controller;

import com.colak.springrestmockmvctutorial.model.Book;
import com.colak.springrestmockmvctutorial.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public Book create(@RequestBody Book book) {
        return bookService.create(book);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> getById(@PathVariable Long bookId) {
        Optional<Book> bookOptional = bookService.getById(bookId);

        return bookOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/book")
    public boolean update(@RequestBody Book book) {
        return bookService.update(book);
    }

    @DeleteMapping("/{bookId}")
    public boolean delete(@PathVariable Long bookId) {
        return bookService.delete(bookId);
    }
}
