package com.example.myapplication3.Retrofit;

import com.example.myapplication3.model.quotes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitService {

    @GET("/quotes")
    Call<List<quotes>> getAllQuotes();

    @POST("/quotes")
    Call<quotes> createQuote(@Body quotes quote);

    @GET("/quotes/{id}")
    Call<quotes> getQuoteById(@Path("id") String id);

    @PUT("quotes/{id}")
    Call<Void> updateQuoteById(@Path("id") String id, @Body quotes updatedQuote);


    @DELETE("/quotes/{id}")
    Call<Void> deleteQuoteById(@Path("id") String id);

    @DELETE("/quotes/{author}")
    Call<Void> deleteQuoteByAuthor(@Path("author") String authorToDelete);

    @GET("/quotes")
    Call<List<quotes>> getQuotesByAuthor(@Query("author") String authorToDelete);
}
