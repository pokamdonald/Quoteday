package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication3.Retrofit.QuoteApi;
import com.example.myapplication3.Retrofit.RetrofitService;
import com.example.myapplication3.model.quotes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewQuote extends AppCompatActivity {

    private Button buttonGoBack;
    private RecyclerView recyclerViewQuotes;
    private QuoteAdapter quoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewquote);

        buttonGoBack = findViewById(R.id.buttonGoBack);
        recyclerViewQuotes = findViewById(R.id.recyclerViewQuotes);

        // Set up RecyclerView with vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewQuotes.setLayoutManager(layoutManager);

        // Initialize and set up QuoteAdapter
        quoteAdapter = new QuoteAdapter();
        recyclerViewQuotes.setAdapter(quoteAdapter);

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToInfosPage();
            }
        });

        // Retrofit code to get all quotes
        RetrofitService retrofitService = QuoteApi.getRetrofitInstance().create(RetrofitService.class);
        Call<List<quotes>> call = retrofitService.getAllQuotes();

        call.enqueue(new Callback<List<quotes>>() {
            @Override
            public void onResponse(Call<List<quotes>> call, Response<List<quotes>> response) {
                if (response.isSuccessful()) {
                    List<quotes> quoteList = response.body();
                    displayQuotes(quoteList);
                } else {
                    Log.e("ViewQuote", "Failed to get quotes. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<quotes>> call, Throwable t) {
                Log.e("ViewQuote", "Failed to get quotes. Error: " + t.getMessage());
            }
        });
    }

    private void goBackToInfosPage() {
        Intent intent = new Intent(this, INFOSPAGE.class);
        startActivity(intent);
    }

    private void displayQuotes(List<quotes> quoteList) {
        // Clear existing views
        Log.d("ViewQuote", "Displaying quotes. Count: " + quoteList.size());

        // Set quote numbers for display
        for (int i = 0; i < quoteList.size(); i++) {
            quoteList.get(i).setQuoteNumber(i + 1);
        }

        quoteAdapter.setQuoteList(quoteList);
    }
}
