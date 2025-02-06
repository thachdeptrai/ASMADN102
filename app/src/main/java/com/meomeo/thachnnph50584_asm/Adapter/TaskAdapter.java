package com.meomeo.thachnnph50584_asm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meomeo.thachnnph50584_asm.Model.Task;
import com.meomeo.thachnnph50584_asm.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private Context context;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvName.setText(task.getName());
        holder.tvContent.setText(task.getContent());
        // Chuyển trạng thái thành text
        String statusText;
        switch (task.getStatus()) {
            case 0:
                statusText = "Mới tạo";
                break;
            case 1:
                statusText = "Đang làm";
                break;
            case 2:
                statusText = "Hoàn thành";
                break;
            case -1:
                statusText = "Trong thùng rác";
                break;
            default:
                statusText = "Không xác định";
        }
        holder.tvStatus.setText(statusText);
        holder.tvStart.setText("Bắt đầu: " + task.getStart());
        holder.tvEnd.setText("Kết thúc: " + task.getEnd());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContent, tvStatus, tvStart, tvEnd;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTaskName);
            tvContent = itemView.findViewById(R.id.tvTaskContent);
            tvStatus = itemView.findViewById(R.id.tvTaskStatus);
            tvStart = itemView.findViewById(R.id.tvTaskStart);
            tvEnd = itemView.findViewById(R.id.tvTaskEnd);
        }
    }
}