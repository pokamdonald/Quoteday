package com.quote.quoteday.model;

import org.springframework.data.annotation.Id;

public class quotes {

    @Id
    private String id;

    private String author;
    private String quote;
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public quotes(String id, String author, String quote, String createdAt) {
        this.id = id;
        this.author = author;
        this.quote = quote;
        this.createdAt = createdAt;
    }
}
