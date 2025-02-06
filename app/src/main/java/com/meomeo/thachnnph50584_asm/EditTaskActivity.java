package com.meomeo.thachnnph50584_asm;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.Model.Task;
import com.meomeo.thachnnph50584_asm.R;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etTaskName, etTaskContent, etStartDate, etEndDate;
    private Button btnUpdate;
    private NNTDB dbHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTaskName = findViewById(R.id.etTaskName);
        etTaskContent = findViewById(R.id.etTaskContent);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnUpdate = findViewById(R.id.btnUpdateTask);
        dbHelper = new NNTDB(this);

        // Lấy taskId từ Intent
        taskId = getIntent().getIntExtra("taskId", -1);
        if(taskId == -1){
            Toast.makeText(this, "Task không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadTaskInfo(taskId);

        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));

        btnUpdate.setOnClickListener(v -> {
            String name = etTaskName.getText().toString().trim();
            String content = etTaskContent.getText().toString().trim();
            String start = etStartDate.getText().toString().trim();
            String end = etEndDate.getText().toString().trim();
            if(name.isEmpty() || content.isEmpty() || start.isEmpty() || end.isEmpty()){
                Toast.makeText(EditTaskActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Task updatedTask = new Task(taskId, name, content, 0, start, end);
                int rows = dbHelper.updateTask(updatedTask);
                if(rows > 0){
                    Toast.makeText(EditTaskActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditTaskActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadTaskInfo(int taskId){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + NNTDB.TABLE_TASK + " WHERE " + NNTDB.COLUMN_TASK_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(taskId) });
        if(cursor != null && cursor.moveToFirst()){
            etTaskName.setText(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_NAME)));
            etTaskContent.setText(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_CONTENT)));
            etStartDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_START)));
            etEndDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_END)));
            cursor.close();
        }
        database.close();
    }

    private void showDatePickerDialog(final EditText editText){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
