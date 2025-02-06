package com.meomeo.thachnnph50584_asm.Fragment;
import android.content.ContentValues;
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

import com.meomeo.thachnnph50584_asm.Adapter.TaskAdapter;
import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.Model.Task;
import com.meomeo.thachnnph50584_asm.R;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private Button btnAdd, btnEdit, btnDelete;

    public TaskManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_task_management, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load danh sách công việc từ SQLite
        loadTasks();

        // Thêm công việc mới
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Ví dụ: tạo một task mới với dữ liệu mẫu
                Task newTask = new Task(0, "Task mới", "Nội dung task mới", 0, "2025-02-10", "2025-02-15");
                addTask(newTask);
                loadTasks(); // cập nhật lại danh sách
            }
        });

        // Sửa công việc đầu tiên (nếu có)
        btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!taskList.isEmpty()){
                    Task task = taskList.get(0);
                    task.setName(task.getName() + " (Đã sửa)");
                    updateTask(task);
                    loadTasks();
                }
            }
        });

        // Xóa công việc cuối cùng (nếu có)
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!taskList.isEmpty()){
                    Task task = taskList.get(taskList.size()-1);
                    deleteTask(task);
                    loadTasks();
                }
            }
        });

        return view;
    }

    // Phương thức load danh sách task từ DB
    private void loadTasks(){
        taskList = new ArrayList<>();
        NNTDB dbHelper = new NNTDB(getContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + NNTDB.TABLE_TASK;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(NNTDB.COLUMN_TASK_ID));
                String name = cursor.getString(cursor.getColumnIndex(NNTDB.COLUMN_TASK_NAME));
                String content = cursor.getString(cursor.getColumnIndex(NNTDB.COLUMN_TASK_CONTENT));
                int status = cursor.getInt(cursor.getColumnIndex(NNTDB.COLUMN_TASK_STATUS));
                String start = cursor.getString(cursor.getColumnIndex(NNTDB.COLUMN_TASK_START));
                String end = cursor.getString(cursor.getColumnIndex(NNTDB.COLUMN_TASK_END));
                taskList.add(new Task(id, name, content, status, start, end));
            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();

        // Cập nhật Adapter
        taskAdapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(taskAdapter);
    }

    // Thêm Task vào DB
    private void addTask(Task task){
        NNTDB dbHelper = new NNTDB(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NNTDB.COLUMN_TASK_NAME, task.getName());
        values.put(NNTDB.COLUMN_TASK_CONTENT, task.getContent());
        values.put(NNTDB.COLUMN_TASK_STATUS, task.getStatus());
        values.put(NNTDB.COLUMN_TASK_START, task.getStart());
        values.put(NNTDB.COLUMN_TASK_END, task.getEnd());
        long id = database.insert(NNTDB.TABLE_TASK, null, values);
        task.setId((int) id);
        database.close();
    }

    // Cập nhật Task trong DB
    private void updateTask(Task task){
        NNTDB dbHelper = new NNTDB(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NNTDB.COLUMN_TASK_NAME, task.getName());
        values.put(NNTDB.COLUMN_TASK_CONTENT, task.getContent());
        values.put(NNTDB.COLUMN_TASK_STATUS, task.getStatus());
        values.put(NNTDB.COLUMN_TASK_START, task.getStart());
        values.put(NNTDB.COLUMN_TASK_END, task.getEnd());
        database.update(NNTDB.TABLE_TASK, values, NNTDB.COLUMN_TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        database.close();
    }

    // Xóa Task khỏi DB
    private void deleteTask(Task task){
        NNTDB dbHelper = new NNTDB(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(NNTDB.TABLE_TASK, NNTDB.COLUMN_TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        database.close();
    }
}