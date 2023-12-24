package com.quote.quoteday.Controller;

import com.quote.quoteday.model.quotes;
import com.quote.quoteday.repository.quoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quotes")
public class quoteController {

    private final quoteRepository repository;

    @Autowired
    public quoteController(quoteRepository repository) {
        this.repository = repository;
    }


    @PostMapping
    public quotes createQuote(@RequestBody quotes quote) {
        return repository.save(quote);
    }

    // Get all quotes
    @GetMapping
    public List<quotes> getAllQuotes() {
        return repository.findAll();
    }

    // Get a specific quote by ID
    @GetMapping("/{id}")
    public Optional<quotes> getQuoteById(@PathVariable String id) {
        return repository.findById(id);
    }

    // Update a quote by ID
    @PutMapping("/{id}")
    public quotes updateQuote(@PathVariable String id, @RequestBody quotes updatedQuote) {
        updatedQuote.setId(id);
        return repository.save(updatedQuote);
    }

    // Delete a quote by ID
    @DeleteMapping("/{id}")
    public void deleteQuote(@PathVariable String id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/deleteByAuthor")
    public void deleteQuotesByAuthor(@RequestParam String author) {
        repository.deleteByAuthor(author);
    }
}
