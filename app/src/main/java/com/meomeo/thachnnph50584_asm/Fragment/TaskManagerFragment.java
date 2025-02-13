package com.meomeo.thachnnph50584_asm.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meomeo.thachnnph50584_asm.Activity.AddTaskActivity;
import com.meomeo.thachnnph50584_asm.Adapter.TaskAdapter;
import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.Activity.EditTaskActivity;
import com.meomeo.thachnnph50584_asm.Model.Task;
import com.meomeo.thachnnph50584_asm.R;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private Button btnAdd;
    private NNTDB dbHelper;

    public TaskManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_task_management, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new NNTDB(getContext());
        loadTasks();

        // Nút thêm task
        btnAdd.setOnClickListener(v -> {
            // Mở AddTaskActivity để nhập thông tin mới
            startActivity(new Intent(getContext(), AddTaskActivity.class));
        });

        return view;
    }

    private void loadTasks(){
        taskList = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + NNTDB.TABLE_TASK;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_NAME));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_CONTENT));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_STATUS));
                String start = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_START));
                String end = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_END));
                taskList.add(new Task(id, name, content, status, start, end));
            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();

        taskAdapter = new TaskAdapter(getContext(), taskList, new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onEdit(Task task) {
                // Mở EditTaskActivity, truyền task cần sửa
                Intent intent = new Intent(getContext(), EditTaskActivity.class);
                intent.putExtra("taskId", task.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Task task) {
                // Xóa task và reload danh sách
                dbHelper.deleteTask(task.getId());
                loadTasks();
            }
        });
        recyclerView.setAdapter(taskAdapter);
    }
}
