package com.example.deepstudy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepstudy.api.ApiService;
import com.example.deepstudy.model.Quote;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    private TextView tvQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_deep_study);

        Button btnBackSession = findViewById(R.id.btnBackSession);
        Button btnShare = findViewById(R.id.btnShare);

        tvQuote = findViewById(R.id.tvQuote);

        loadQuoteFromApi();

        btnBackSession.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String quoteText = tvQuote.getText().toString();
            String shareBody = "Baru saja fokus 100%! \n\n" + quoteText + "\n\n#DeepStudy";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hasil Belajar Saya");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Bagikan lewat"));
        });
    }

    private void loadQuoteFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zenquotes.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getRandomQuote().enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Quote q = response.body().get(0);
                    String fullText = "\"" + q.getText() + "\"\n- " + q.getAuthor();
                    tvQuote.setText(fullText);
                } else {
                    tvQuote.setText("Great job! Keep pushing!");
                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                tvQuote.setText("You did great today!");
            }
        });
    }
}