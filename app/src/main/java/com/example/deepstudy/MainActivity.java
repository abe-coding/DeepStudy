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

    // 1. Deklarasi Variabel
    private MaterialCardView card25Min, card50Min;
    private Button btn1x, btn2x, btn3x;
    private Button btnSave;

    // Variabel untuk menyimpan pilihan user
    private int selectedDuration = 25; // Default 25
    private int selectedRepeat = 1;    // Default 1x

    // Warna (Diambil dari colors.xml)
    private int colorMint, colorUnselected, colorTextMint, colorTextSec, colorTextWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Inisialisasi Warna
        colorMint = ContextCompat.getColor(this, R.color.mint_accent);
        colorUnselected = ContextCompat.getColor(this, R.color.card_unselected);
        colorTextMint = ContextCompat.getColor(this, R.color.mint_text);
        colorTextSec = ContextCompat.getColor(this, R.color.text_secondary);
        colorTextWhite = ContextCompat.getColor(this, R.color.text_white);

        // 3. Inisialisasi View
        card25Min = findViewById(R.id.card25Min);
        card50Min = findViewById(R.id.card50Min);
        btn1x = findViewById(R.id.btn1x); // Pastikan tambah ID di XML
        btn2x = findViewById(R.id.btn2x); // Pastikan tambah ID di XML
        btn3x = findViewById(R.id.btn3x); // Pastikan tambah ID di XML
        btnSave = findViewById(R.id.btnSaveSession);

        // 4. Set Listener untuk Durasi
        card25Min.setOnClickListener(v -> updateDurationUI(25));
        card50Min.setOnClickListener(v -> updateDurationUI(50));

        // 5. Set Listener untuk Repeat
        btn1x.setOnClickListener(v -> updateRepeatUI(1));
        btn2x.setOnClickListener(v -> updateRepeatUI(2));
        btn3x.setOnClickListener(v -> updateRepeatUI(3));

        // 6. Tombol Save Pindah Halaman
        btnSave.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
            // Kita bisa kirim data ini ke halaman berikutnya
            intent.putExtra("DURATION", selectedDuration);
            intent.putExtra("REPEAT", selectedRepeat);
            startActivity(intent);
        });

        // Set tampilan awal
        updateDurationUI(25);
        updateRepeatUI(1);
    }

    // Fungsi Logika Ganti Warna Durasi
    private void updateDurationUI(int duration) {
        selectedDuration = duration;

        if (duration == 25) {
            // Aktifkan 25, Matikan 50
            card25Min.setCardBackgroundColor(colorMint);
            card50Min.setCardBackgroundColor(colorUnselected);
            // Ubah text color jika perlu (opsional, sesuaikan id textview di xml)
        } else {
            // Aktifkan 50, Matikan 25
            card25Min.setCardBackgroundColor(colorUnselected);
            card50Min.setCardBackgroundColor(colorMint);
        }
    }

    // Fungsi Logika Ganti Warna Repeat
    private void updateRepeatUI(int repeat) {
        selectedRepeat = repeat;

        // Reset semua ke warna mati dulu
        btn1x.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
        btn1x.setTextColor(colorTextSec);
        btn2x.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
        btn2x.setTextColor(colorTextSec);
        btn3x.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
        btn3x.setTextColor(colorTextSec);

        // Nyalakan yang dipilih
        Button selectedBtn = (repeat == 1) ? btn1x : (repeat == 2) ? btn2x : btn3x;
        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(colorMint));
        selectedBtn.setTextColor(colorTextMint);
    }
}