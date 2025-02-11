package com.meomeo.thachnnph50584_asm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Spinner spinnerStatus;
    private String[] statusNames = {"Mới tạo", "Đang làm", "Hoàn thành", "Trong thùng rác"};
    // Mảng giá trị tương ứng: 0, 1, 2, -1
    private int[] statusValues = {0, 1, 2, -1};
    private int selectedStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTaskName = findViewById(R.id.etTaskName);
        etTaskContent = findViewById(R.id.etTaskContent);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnUpdate = findViewById(R.id.btnUpdateTask);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        dbHelper = new NNTDB(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setSelection(0); // mặc định là "Mới tạo"
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statusValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStatus = statusValues[0];
            }
        });
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
                Task updatedTask = new Task(taskId, name, content, selectedStatus, start, end);
                int rows = dbHelper.updateTask(updatedTask);
                if(rows > 0){
                    startActivity(new Intent(EditTaskActivity.this, HomeActivity.class));
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
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_STATUS));
            for (int i = 0; i < statusValues.length; i++) {
                if (statusValues[i] == status) {
                    spinnerStatus.setSelection(i);
                    selectedStatus = status;
                    break;
                }
            }
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
