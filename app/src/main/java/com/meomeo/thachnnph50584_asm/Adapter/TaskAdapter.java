package com.meomeo.thachnnph50584_asm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meomeo.thachnnph50584_asm.Model.Task;
import com.meomeo.thachnnph50584_asm.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private Context context;
    private OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onEdit(Task task);
        void onDelete(Task task);
    }
    public TaskAdapter(Context context, List<Task> tasks,OnTaskActionListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
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
        holder.tvTaskName.setText(task.getName());
        holder.tvTaskContent.setText(task.getContent());
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
        holder.tvTaskName.setText(task.getName());
        holder.tvTaskStatus.setText(statusText);
        holder.tvTaskContent.setText(task.getContent());
        holder.tvTaskTime.setText("Bắt đầu: " + task.getStart() + "  -  Kết thúc: " + task.getEnd());
        holder.btnEditTask.setTag(task.getId());
        holder.btnDeleteTask.setTag(task.getId());

        // Sự kiện click edit
        holder.btnEditTask.setOnClickListener(v -> {
            int id = (int) v.getTag();
            if (listener != null) {
                listener.onEdit(task);
            }
        });
        // Sự kiện click delete
        holder.btnDeleteTask.setOnClickListener(v -> {
            int id = (int) v.getTag();
            if (listener != null) {
                listener.onDelete(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskName, tvTaskContent, tvTaskTime, tvTaskStatus;
        ImageButton btnEditTask, btnDeleteTask;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvTaskContent = itemView.findViewById(R.id.tvTaskContent);
            tvTaskStatus = itemView.findViewById(R.id.tvTaskStatus);
            tvTaskTime = itemView.findViewById(R.id.tvTaskTime);
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
        }
    }
}