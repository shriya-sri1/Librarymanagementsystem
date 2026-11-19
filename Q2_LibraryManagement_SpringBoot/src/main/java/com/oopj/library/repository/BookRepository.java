package com.oopj.library.repository;

import com.oopj.library.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) ->
            new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("available_copies")
            );

    public int save(Book book) {
        String sql = "INSERT INTO books(id, title, author, category, available_copies) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, book.getId(), book.getTitle(), book.getAuthor(),
                book.getCategory(), book.getAvailableCopies());
    }

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM books ORDER BY id", bookRowMapper);
    }

    public Optional<Book> findById(Integer id) {
        List<Book> result = jdbcTemplate.query("SELECT * FROM books WHERE id = ?", bookRowMapper, id);
        return result.stream().findFirst();
    }

    public int update(Integer id, Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, available_copies = ? WHERE id = ?";
        return jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(),
                book.getCategory(), book.getAvailableCopies(), id);
    }

    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }
}
