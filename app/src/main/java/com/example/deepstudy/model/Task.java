package com.example.deepstudy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    // Field ini untuk logika frontend (warna berubah saat dipilih)
    public boolean isSelected = false;

    public Task(String title) {
        this.title = title;
    }
}