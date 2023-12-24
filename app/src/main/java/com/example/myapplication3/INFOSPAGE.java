package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication3.Retrofit.QuoteApi;
import com.example.myapplication3.Retrofit.RetrofitService;
import com.example.myapplication3.model.quotes;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class INFOSPAGE extends AppCompatActivity {

    private EditText editTextQuote;
    private EditText editTextAuthor;
    private EditText editTextCreatedDate;
    private EditText editTextText;
    private Button buttonPickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infospage);

        // Initialize EditTexts
        editTextQuote = findViewById(R.id.editTextQuote);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextCreatedDate = findViewById(R.id.editTextCreatedDate);
        editTextText = findViewById(R.id.editTextText);
        buttonPickDate = findViewById(R.id.buttonPickDate);

        // Buttons for Actions
        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        Button buttonView = findViewById(R.id.buttonView);

        // Retrofit code to handle actions
        RetrofitService retrofitService = QuoteApi.getRetrofitInstance().create(RetrofitService.class);

        // Date picker dialog
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the EditText with the selected date
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editTextCreatedDate.setText(selectedDate);
            }
        };

        // Set onClickListener for the date picker button
        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog
                showDatePickerDialog(dateSetListener);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrofit code to add a quote
                String quoteText = editTextQuote.getText().toString();
                String authorName = editTextAuthor.getText().toString();
                String createdDate = editTextCreatedDate.getText().toString();

                // Create a new quotes object
                quotes newQuote = new quotes(null, quoteText, authorName, createdDate);

                Call<quotes> call = retrofitService.createQuote(newQuote);

                call.enqueue(new Callback<quotes>() {
                    @Override
                    public void onResponse(Call<quotes> call, Response<quotes> response) {
                        if (response.isSuccessful()) {
                            quotes addedQuote = response.body();
                            Log.d("INFOSPAGE", "Added quote: " + addedQuote);

                            // Display a Toast message for successful addition
                            Toast.makeText(INFOSPAGE.this, "Quote added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("INFOSPAGE", "Failed to add quote. Code: " + response.code());

                            // Display a Toast message for failure
                            Toast.makeText(INFOSPAGE.this, "Failed to add quote", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quotes> call, Throwable t) {
                        Log.e("INFOSPAGE", "Failed to add quote. Error: " + t.getMessage());

                        // Display a Toast message for failure
                        Toast.makeText(INFOSPAGE.this, "Failed to add quote", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quoteIdToDelete = editTextText.getText().toString();

                if (!quoteIdToDelete.isEmpty()) {
                    Call<Void> deleteCall = retrofitService.deleteQuoteById(quoteIdToDelete);

                    deleteCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("INFOSPAGE", "Quote deleted successfully");
                                Toast.makeText(INFOSPAGE.this, "Quote deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("INFOSPAGE", "Failed to delete quote. Code: " + response.code());
                                Toast.makeText(INFOSPAGE.this, "Failed to delete quote", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("INFOSPAGE", "Failed to delete quote. Error: " + t.getMessage());
                            Toast.makeText(INFOSPAGE.this, "Failed to delete quote", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("INFOSPAGE", "Quote ID to delete is empty");
                    Toast.makeText(INFOSPAGE.this, "Quote ID to delete is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quoteIdToUpdate = editTextText.getText().toString();

                if (!quoteIdToUpdate.isEmpty()) {

                    String updatedQuoteText = editTextQuote.getText().toString();
                    String updatedAuthorName = editTextAuthor.getText().toString();
                    String updatedCreatedDate = editTextCreatedDate.getText().toString();


                    quotes updatedQuote = new quotes(quoteIdToUpdate, updatedQuoteText, updatedAuthorName, updatedCreatedDate);


                    Call<Void> call = retrofitService.updateQuoteById(quoteIdToUpdate, updatedQuote);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("INFOSPAGE", "Quote updated successfully");


                                Toast.makeText(INFOSPAGE.this, "Quote updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("INFOSPAGE", "Failed to update quote. Code: " + response.code());


                                Toast.makeText(INFOSPAGE.this, "Failed to update quote", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("INFOSPAGE", "Failed to update quote. Error: " + t.getMessage());


                            Toast.makeText(INFOSPAGE.this, "Failed to update quote", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("INFOSPAGE", "Quote ID to update is empty");

                   
                    Toast.makeText(INFOSPAGE.this, "Quote ID to update is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the ViewQuote activity without passing a specific quote ID
                Intent intent = new Intent(INFOSPAGE.this, ViewQuote.class);
                startActivity(intent);
            }
        });

    }


    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                year,
                month,
                day
        );
        datePickerDialog.show();
    }
}
