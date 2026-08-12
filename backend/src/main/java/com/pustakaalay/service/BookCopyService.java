package com.pustakaalay.service;

import com.pustakaalay.dto.BookCopyRequest;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.entity.Book;
import com.pustakaalay.entity.BookCopy;
import com.pustakaalay.repository.BookCopyRepository;
import com.pustakaalay.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(
            BookCopyRepository bookCopyRepository,
            BookRepository bookRepository
    ) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BookCopy createBookCopy(BookCopyRequest request) {

        if (bookCopyRepository.existsByBarcode(request.getBarcode())) {
            throw new ConflictException(
                    "A book copy with barcode '" +
                    request.getBarcode() +
                    "' already exists"
            );
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " +
                                request.getBookId()
                        )
                );

        BookCopy bookCopy = new BookCopy();

        applyRequest(bookCopy, request, book);

        return bookCopyRepository.save(bookCopy);
    }

    @Transactional(readOnly = true)
    public List<BookCopy> getAllBookCopies() {
        return bookCopyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BookCopy getBookCopyById(Long id) {
        return bookCopyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book copy not found with id: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public BookCopy getByBarcode(String barcode) {
        return bookCopyRepository.findByBarcode(barcode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book copy not found with barcode: " + barcode
                        )
                );
    }

    @Transactional
    public BookCopy updateBookCopy(
            Long id,
            BookCopyRequest request
    ) {

        BookCopy bookCopy = getBookCopyById(id);

        bookCopyRepository.findByBarcode(request.getBarcode())
                .filter(existingCopy ->
                        !existingCopy.getId().equals(id)
                )
                .ifPresent(existingCopy -> {
                    throw new IllegalArgumentException(
                            "A book copy with barcode '" +
                            request.getBarcode() +
                            "' already exists"
                    );
                });

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " +
                                request.getBookId()
                        )
                );

        applyRequest(bookCopy, request, book);

        return bookCopyRepository.save(bookCopy);
    }

    @Transactional
    public void deleteBookCopy(Long id) {
        BookCopy bookCopy = getBookCopyById(id);
        bookCopyRepository.delete(bookCopy);
    }

    @Transactional(readOnly = true)
    public List<BookCopy> getCopiesByBookId(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId
            );
        }

        return bookCopyRepository.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public List<BookCopy> getCopiesByStatus(
            BookCopy.CopyStatus status
    ) {
        return bookCopyRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countCopiesByBook(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId
            );
        }

        return bookCopyRepository.countByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public long countAvailableCopiesByBook(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId
            );
        }

        return bookCopyRepository.countByBookIdAndStatus(
                bookId,
                BookCopy.CopyStatus.AVAILABLE
        );
    }

    private void applyRequest(
            BookCopy bookCopy,
            BookCopyRequest request,
            Book book
    ) {

        bookCopy.setBook(book);
        bookCopy.setBarcode(request.getBarcode());
        bookCopy.setAcquisitionDate(request.getAcquisitionDate());
        bookCopy.setPrice(request.getPrice());

        if (request.getStatus() != null) {
            bookCopy.setStatus(request.getStatus());
        }

        if (request.getConditionStatus() != null) {
            bookCopy.setConditionStatus(
                    request.getConditionStatus()
            );
        }

        bookCopy.setLocation(request.getLocation());
    }
}
