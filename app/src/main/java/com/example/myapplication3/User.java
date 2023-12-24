
package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication3.R;
import com.example.myapplication3.Retrofit.QuoteApi;
import com.example.myapplication3.Retrofit.RetrofitService;
import com.example.myapplication3.model.quotes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User extends AppCompatActivity {

    private TextView quoteTextView;
    private TextView authorTextView;
    private Button nextButton;
    private Button previousButton;
    private Button shareButton;

    private int currentQuoteIndex = 0;
    private List<String> quoteIds = new ArrayList<>(); // List to store quote IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize TextViews
        quoteTextView = findViewById(R.id.quoteTextView);
        authorTextView = findViewById(R.id.authorTextView);

        // Initialize Buttons
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        shareButton = findViewById(R.id.shareButton);

        // Set onClickListeners for buttons
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextQuote();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousQuote();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQuoteAndAuthor();
            }
        });

        // Fetch and display quote and author information
        fetchQuotes();
    }

    // Method to fetch all quotes and extract quote IDs
    private void fetchQuotes() {
        RetrofitService retrofitService = QuoteApi.getRetrofitInstance().create(RetrofitService.class);

        Call<List<quotes>> call = retrofitService.getAllQuotes();

        call.enqueue(new Callback<List<quotes>>() {
            @Override
            public void onResponse(Call<List<quotes>> call, Response<List<quotes>> response) {
                if (response.isSuccessful()) {
                    List<quotes> quotesList = response.body();
                    if (quotesList != null && !quotesList.isEmpty()) {
                        // Extract quote IDs from the fetched quotes
                        for (quotes quote : quotesList) {
                            quoteIds.add(quote.getId());
                        }

                        // Display the initial quote and author
                        fetchQuoteAndAuthor();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<quotes>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Method to fetch quote and author information from the database
    // Method to fetch quote and author information from the database
    private void fetchQuoteAndAuthor() {
        RetrofitService retrofitService = QuoteApi.getRetrofitInstance().create(RetrofitService.class);

        // Fetch the current quote using the current index
        String quoteIdToFetch = quoteIds.get(currentQuoteIndex);

        Call<quotes> call = retrofitService.getQuoteById(quoteIdToFetch);

        call.enqueue(new Callback<quotes>() {
            @Override
            public void onResponse(Call<quotes> call, Response<quotes> response) {
                if (response.isSuccessful()) {
                    quotes fetchedQuote = response.body();
                    if (fetchedQuote != null) {
                        // Display the quote and author in the TextViews
                        String formattedQuote = "\"" + fetchedQuote.getQuote() + "\"";
                        quoteTextView.setText(formattedQuote);

                        // Modify the author text to include "Author: "
                        String authorText = "Author: " + fetchedQuote.getAuthor();
                        authorTextView.setText(authorText);
                    }
                }
            }

            @Override
            public void onFailure(Call<quotes> call, Throwable t) {
                // Handle failure
            }
        });
    }


    // Method to show the next quote
    private void showNextQuote() {
        if (currentQuoteIndex < quoteIds.size() - 1) {
            currentQuoteIndex++;
            fetchQuoteAndAuthor();
        } else {
            Toast.makeText(this, "No more quotes available", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to show the previous quote
    private void showPreviousQuote() {
        if (currentQuoteIndex > 0) {
            currentQuoteIndex--;
            fetchQuoteAndAuthor();
        } else {
            Toast.makeText(this, "No previous quotes available", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to share the current quote and author
    // Method to share the current quote and author
    private void shareQuoteAndAuthor() {
        String currentQuote = quoteTextView.getText().toString();
        String currentAuthor = authorTextView.getText().toString();

        // Create an Intent with action ACTION_SEND
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        shareIntent.setType("text/plain");

        // Set the subject and text of the sharing
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quote from " + currentAuthor);
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentQuote + "\n\nAuthor: " + currentAuthor);

        // Start the activity with the Intent
        startActivity(Intent.createChooser(shareIntent, "Share using"));
    }

}
