package com.example.deepstudy;

import android.content.Intent; // Jangan lupa import ini
import android.os.Bundle;
import android.widget.Button;
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

public class CreateTaskActivity extends AppCompatActivity {

    // UI Components
    private TextInputEditText etTaskTitle;
    private Button btnCreate;
    private Button btnStartSession; // TAMBAHAN: Button Start
    private RecyclerView recyclerView;

    // Database & Adapter
    private TaskAdapter adapter;
    private AppDatabase database;
    private List<Task> taskList;

    // Variabel untuk menyimpan data dari Halaman 1
    private int receivedDuration = 25; // Default 25
    private int receivedRepeat = 1;    // Default 1x

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // --- 1. TERIMA DATA DARI MAIN ACTIVITY ---
        // "DURATION" dan "REPEAT" harus sama persis hurufnya dengan key di MainActivity
        receivedDuration = getIntent().getIntExtra("DURATION", 25);
        receivedRepeat = getIntent().getIntExtra("REPEAT", 1);

        // 2. Inisialisasi Komponen UI
        etTaskTitle = findViewById(R.id.etTaskTitle);
        btnCreate = findViewById(R.id.btnCreateTask);
        btnStartSession = findViewById(R.id.btnStartSession); // TAMBAHAN: Inisialisasi ID
        recyclerView = findViewById(R.id.recyclerViewTasks);

        // 3. Inisialisasi Database
        database = AppDatabase.getDbInstance(this);

        // 4. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();

        adapter = new TaskAdapter(this, taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Toast.makeText(CreateTaskActivity.this, "Selected: " + task.title, Toast.LENGTH_SHORT).show();
                // Nanti disini kamu bisa simpan ID task yang dipilih kalau mau
            }
        });
        recyclerView.setAdapter(adapter);

        // 5. Load Data Awal
        loadTasks();

        // 6. Logic Tombol Create (Simpan ke DB)
        btnCreate.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString();
            if (!title.isEmpty()) {
                saveNewTask(title);
            }
        });

        // --- 7. LOGIC TOMBOL START SESSION (PINDAH HALAMAN) ---
        btnStartSession.setOnClickListener(v -> {
            // Pindah ke MoveTaskActivity
            Intent intent = new Intent(CreateTaskActivity.this, MoveTaskActivity.class);

            // Oper lagi data Durasi ke halaman berikutnya
            intent.putExtra("DURATION", receivedDuration);
            intent.putExtra("REPEAT", receivedRepeat);

            startActivity(intent);
        });
    }

    private void saveNewTask(String title) {
        Task newTask = new Task(title);
        database.taskDao().insert(newTask);
        etTaskTitle.setText("");
        loadTasks();
    }

    private void loadTasks() {
        taskList = database.taskDao().getAllTasks();
        adapter.setTaskList(taskList);
    }
}