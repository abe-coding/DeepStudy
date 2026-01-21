package com.example.deepstudy;

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

    private RecyclerView recyclerView;
    private TextInputEditText etTaskInput;
    private TextView tvTimer;
    private ImageButton btnBack;

    private TaskAdapter adapter;
    private AppDatabase database;
    private List<Task> taskList;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_task);

        initViews();

        setupDatabase();

        int durationMinutes = getIntent().getIntExtra("DURATION", 25); // Default 25 menit
        startTimer(durationMinutes);

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

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvMoveTask);
        etTaskInput = findViewById(R.id.etTaskInput);
        tvTimer = findViewById(R.id.tvTimerVal);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupDatabase() {
        database = AppDatabase.getDbInstance(this);
        taskList = new ArrayList<>();

        adapter = new TaskAdapter(this, taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
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
        etTaskInput.setText("");
        loadTasks();
        Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
    }

    private void startTimer(int minutes) {
        timeLeftInMillis = minutes * 60 * 1000;

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
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}