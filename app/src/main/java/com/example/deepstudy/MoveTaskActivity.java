package com.example.deepstudy; // Sesuaikan dengan package kamu

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepstudy.adapter.TaskAdapter;
import com.example.deepstudy.database.AppDatabase;
import com.example.deepstudy.model.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MoveTaskActivity extends AppCompatActivity {

    // Komponen UI
    private RecyclerView recyclerView;
    private TextInputEditText etTaskInput;
    private TextView tvTimer;
    private ImageButton btnBack;

    // Database & Adapter
    private TaskAdapter adapter;
    private AppDatabase database;
    private List<Task> taskList;

    // Timer logic
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_task);

        // 1. Inisialisasi View
        initViews();

        // 2. Setup Database & List Task
        setupDatabase();

        // 3. Setup Timer (Ambil data durasi dari halaman sebelumnya)
        int durationMinutes = getIntent().getIntExtra("DURATION", 25); // Default 25 menit
        startTimer(durationMinutes);

        // 4. Setup Input Task (Tekan Enter untuk Add)
        etTaskInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String content = etTaskInput.getText().toString();
                if (!content.isEmpty()) {
                    addNewTask(content);
                }
                return true;
            }
            return false;
        });

        // 5. Tombol Back
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvMoveTask);
        etTaskInput = findViewById(R.id.etTaskInput); // Pastikan ID ini sudah ditambah di XML
        tvTimer = findViewById(R.id.tvTimerVal);     // Pastikan ID ini sudah ditambah di XML
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupDatabase() {
        database = AppDatabase.getDbInstance(this);
        taskList = new ArrayList<>();

        // Menggunakan Adapter yang sama dengan CreateTaskActivity
        adapter = new TaskAdapter(this, taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                // Logika saat task diklik (misalnya menampilkan toast)
                Toast.makeText(MoveTaskActivity.this, "Fokus pada: " + task.title, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadTasks();
    }

    private void loadTasks() {
        taskList = database.taskDao().getAllTasks();
        adapter.setTaskList(taskList);
    }

    private void addNewTask(String title) {
        Task newTask = new Task(title);
        database.taskDao().insert(newTask);
        etTaskInput.setText(""); // Kosongkan input
        loadTasks(); // Refresh list
        Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
    }

    private void startTimer(int minutes) {
        timeLeftInMillis = minutes * 6 * 1; // Ubah menit ke milidetik

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");

                Intent intent = new Intent(MoveTaskActivity.this, ResultActivity.class);
                startActivity(intent);

                // 3. Matikan Activity ini agar user tidak bisa tekan tombol back ke timer yang sudah habis
                finish();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hentikan timer jika aplikasi ditutup agar tidak memakan memori
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}