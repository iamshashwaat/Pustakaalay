package com.pustakaalay.service;

import com.pustakaalay.dto.BookRequest;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.entity.Author;
import com.pustakaalay.entity.Book;
import com.pustakaalay.entity.Category;
import com.pustakaalay.repository.AuthorRepository;
import com.pustakaalay.repository.BookRepository;
import com.pustakaalay.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            CategoryRepository categoryRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Book createBook(BookRequest request) {

        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ConflictException(
                    "A book with ISBN '" + request.getIsbn() + "' already exists"
            );
        }

        Book book = new Book();

        applyRequest(book, request);

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found with id: " + id)
                );
    }

    @Transactional
    public Book updateBook(Long id, BookRequest request) {

        Book book = getBookById(id);

        bookRepository.findByIsbn(request.getIsbn())
                .filter(existingBook -> !existingBook.getId().equals(id))
                .ifPresent(existingBook -> {
                    throw new ConflictException(
                            "A book with ISBN '" + request.getIsbn() + "' already exists"
                    );
                });

        applyRequest(book, request);

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {

        Book book = getBookById(id);

        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query);
    }

    @Transactional(readOnly = true)
    public List<Book> searchByPublisher(String query) {
        return bookRepository.findByPublisherContainingIgnoreCase(query);
    }

    private void applyRequest(Book book, BookRequest request) {

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setEdition(request.getEdition());
        book.setLanguage(request.getLanguage());
        book.setDescription(request.getDescription());
        book.setCoverImageUrl(request.getCoverImageUrl());
        book.setPages(request.getPages());

        if (request.getAuthorIds() != null) {
            List<Author> authors = authorRepository.findAllById(
                    request.getAuthorIds()
            );

            if (authors.size() != request.getAuthorIds().size()) {
                throw new IllegalArgumentException(
                        "One or more author IDs do not exist"
                );
            }

            book.setAuthors(new HashSet<>(authors));
        } else {
            book.setAuthors(new HashSet<>());
        }

        if (request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(
                    request.getCategoryIds()
            );

            if (categories.size() != request.getCategoryIds().size()) {
                throw new IllegalArgumentException(
                        "One or more category IDs do not exist"
                );
            }

            book.setCategories(new HashSet<>(categories));
        } else {
            book.setCategories(new HashSet<>());
        }
    }
}
