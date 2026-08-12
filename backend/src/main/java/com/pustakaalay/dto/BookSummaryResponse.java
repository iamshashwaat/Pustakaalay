package com.pustakaalay.dto;

import com.pustakaalay.entity.Book;

public class BookSummaryResponse {

    private final Long id;
    private final String title;
    private final String isbn;
    private final String publisher;

    public BookSummaryResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.publisher = book.getPublisher();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
}
