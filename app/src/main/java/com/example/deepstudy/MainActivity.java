package com.example.deepstudy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView card25Min, card50Min;
    private Button btn1x, btn2x, btn3x;
    private Button btnSave;

    private int selectedDuration = 25;
    private int selectedRepeat = 1;

    private int colorMint, colorUnselected, colorTextMint, colorTextSec, colorTextWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorMint = ContextCompat.getColor(this, R.color.mint_accent);
        colorUnselected = ContextCompat.getColor(this, R.color.card_unselected);
        colorTextMint = ContextCompat.getColor(this, R.color.mint_text);
        colorTextSec = ContextCompat.getColor(this, R.color.text_secondary);
        colorTextWhite = ContextCompat.getColor(this, R.color.text_white);

        card25Min = findViewById(R.id.card25Min);
        card50Min = findViewById(R.id.card50Min);
        btn1x = findViewById(R.id.btn1x);
        btn2x = findViewById(R.id.btn2x);
        btn3x = findViewById(R.id.btn3x);
        btnSave = findViewById(R.id.btnSaveSession);

        card25Min.setOnClickListener(v -> updateDurationUI(25));
        card50Min.setOnClickListener(v -> updateDurationUI(50));

        btn1x.setOnClickListener(v -> updateRepeatUI(1));
        btn2x.setOnClickListener(v -> updateRepeatUI(2));
        btn3x.setOnClickListener(v -> updateRepeatUI(3));

        btnSave.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
            intent.putExtra("DURATION", selectedDuration);
            intent.putExtra("REPEAT", selectedRepeat);
            startActivity(intent);
        });

        updateDurationUI(25);
        updateRepeatUI(1);
    }

    private void updateDurationUI(int duration) {
        selectedDuration = duration;

        if (duration == 25) {
            card25Min.setCardBackgroundColor(colorMint);
            card50Min.setCardBackgroundColor(colorUnselected);
        } else {
            card25Min.setCardBackgroundColor(colorUnselected);
            card50Min.setCardBackgroundColor(colorMint);
        }
    }

    private void updateRepeatUI(int repeat) {
        selectedRepeat = repeat;

        btn1x.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
        btn1x.setTextColor(colorTextSec);
        btn2x.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
        btn2x.setTextColor(colorTextSec);
        btn3x.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
        btn3x.setTextColor(colorTextSec);

        Button selectedBtn = (repeat == 1) ? btn1x : (repeat == 2) ? btn2x : btn3x;
        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(colorMint));
        selectedBtn.setTextColor(colorTextMint);
    }
}