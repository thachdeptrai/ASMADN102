package com.meomeo.thachnnph50584_asm;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.Model.Task;
import com.meomeo.thachnnph50584_asm.R;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTaskName, etTaskContent, etStartDate, etEndDate;
    private Button btnSave;
    private NNTDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskName = findViewById(R.id.etTaskName);
        etTaskContent = findViewById(R.id.etTaskContent);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnSave = findViewById(R.id.btnSaveTask);
        dbHelper = new NNTDB(this);

        // Thiết lập DatePicker cho start date
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
        // Thiết lập DatePicker cho end date
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));

        btnSave.setOnClickListener(v -> {
            String name = etTaskName.getText().toString().trim();
            String content = etTaskContent.getText().toString().trim();
            String start = etStartDate.getText().toString().trim();
            String end = etEndDate.getText().toString().trim();

            if(name.isEmpty() || content.isEmpty() || start.isEmpty() || end.isEmpty()){
                Toast.makeText(AddTaskActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Status mặc định là 0 (mới tạo)
                Task newTask = new Task(0, name, content, 0, start, end);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                long result = dbHelper.insertTask(newTask);
                database.close();
                if(result != -1){
                    Toast.makeText(AddTaskActivity.this, "Thêm task thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddTaskActivity.this, "Thêm task thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog(final EditText editText){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                (view, year, month, dayOfMonth) -> {
                    // Lưu ý: tháng được trả về từ 0, vì vậy cần +1
                    String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
