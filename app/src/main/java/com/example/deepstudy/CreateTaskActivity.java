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

    private TextInputEditText etTaskTitle;
    private Button btnCreate;
    private Button btnStartSession;
    private RecyclerView recyclerView;

    private TaskAdapter adapter;
    private AppDatabase database;
    private List<Task> taskList;

    private int receivedDuration = 25;
    private int receivedRepeat = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        receivedDuration = getIntent().getIntExtra("DURATION", 25);
        receivedRepeat = getIntent().getIntExtra("REPEAT", 1);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        btnCreate = findViewById(R.id.btnCreateTask);
        btnStartSession = findViewById(R.id.btnStartSession);
        recyclerView = findViewById(R.id.recyclerViewTasks);

        database = AppDatabase.getDbInstance(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();

        adapter = new TaskAdapter(this, taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Toast.makeText(CreateTaskActivity.this, "Selected: " + task.title, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        loadTasks();

        btnCreate.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString();
            if (!title.isEmpty()) {
                saveNewTask(title);
            }
        });

        btnStartSession.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, MoveTaskActivity.class);

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