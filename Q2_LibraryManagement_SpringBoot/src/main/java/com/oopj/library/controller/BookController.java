package com.oopj.library.controller;

import com.oopj.library.model.Book;
import com.oopj.library.repository.BookRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Q2(b): Add a book using POST and @RequestBody.
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        if (!isValidBook(book)) {
            return ResponseEntity.badRequest().body(Map.of("message",
                    "All fields are required and availableCopies must be zero or more."));
        }
        try {
            bookRepository.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "A book with this ID already exists."));
        }
    }

    // Q2(c): Retrieve all books.
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    // Q2(c): Retrieve one book by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        return bookRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Book not found with ID: " + id)));
    }

    // Q2(d): Update book details using PUT.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        if (!isValidBook(book)) {
            return ResponseEntity.badRequest().body(Map.of("message",
                    "All fields are required and availableCopies must be zero or more."));
        }
        if (bookRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Book not found with ID: " + id));
        }
        book.setId(id);
        bookRepository.update(id, book);
        return ResponseEntity.ok(book);
    }

    // Q2(e): Delete a book and return 404 if it does not exist.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        int rows = bookRepository.deleteById(id);
        if (rows > 0) {
            return ResponseEntity.ok(Map.of("message", "Book deleted successfully."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Book not found with ID: " + id));
    }

    private boolean isValidBook(Book book) {
        return book != null
                && book.getId() != null
                && book.getTitle() != null && !book.getTitle().isBlank()
                && book.getAuthor() != null && !book.getAuthor().isBlank()
                && book.getCategory() != null && !book.getCategory().isBlank()
                && book.getAvailableCopies() != null && book.getAvailableCopies() >= 0;
    }
}
