package com.example.deepstudy.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.deepstudy.model.Task;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    List<Task> getAllTasks();

    @Delete
    void delete(Task task);
}