package com.example.deepstudy.api;

import com.example.deepstudy.model.Quote;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // Kita ambil data dari endpoint "random"
    @GET("random")
    Call<List<Quote>> getRandomQuote();
}