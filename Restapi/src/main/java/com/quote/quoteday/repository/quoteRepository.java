package com.quote.quoteday.repository;

import com.quote.quoteday.model.quotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

public interface quoteRepository  extends MongoRepository<quotes, String> {

    @Transactional
    void deleteByAuthor(String author);
}
