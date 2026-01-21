package com.example.deepstudy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deepstudy.R; // Pastikan import R benar
import com.example.deepstudy.model.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private Context context;
    private List<Task> taskList;

    // Listener untuk mengirim event klik ke Activity
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.title);

        // --- LOGIKA FRONTEND BERUBAH DISINI ---
        if (task.isSelected) {
            // Jika dipilih: Background agak putih, Teks Putih, Icon Mint
            holder.container.setBackgroundColor(Color.parseColor("#1AFFFFFF"));
            holder.ivStatus.setColorFilter(Color.parseColor("#D4F2E7")); // Mint
            holder.tvTitle.setTextColor(Color.WHITE);
        } else {
            // Jika tidak dipilih: Background transparan, Teks Abu, Icon Abu
            holder.container.setBackgroundColor(Color.TRANSPARENT);
            holder.ivStatus.setColorFilter(Color.parseColor("#5C6690")); // Abu
            holder.tvTitle.setTextColor(Color.parseColor("#B0B5D6"));
        }

        // Saat item diklik
        holder.itemView.setOnClickListener(v -> {
            // 1. Reset semua jadi false
            for (Task t : taskList) {
                t.isSelected = false;
            }
            // 2. Set item yang diklik jadi true
            task.isSelected = true;

            // 3. Update tampilan (Refresh RecyclerView)
            notifyDataSetChanged();

            // 4. Kirim data ke Activity
            listener.onTaskClick(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivStatus;
        LinearLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            container = itemView.findViewById(R.id.containerTask);
        }
    }
}