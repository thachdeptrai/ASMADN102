package com.meomeo.thachnnph50584_asm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.R;

public class EditAbout  extends AppCompatActivity {

    private EditText etMSSV, etName, etClass, etSubject;
    private ImageView ivStudent;
    private Button btnSave;
private String currentEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about);

        etMSSV = findViewById(R.id.etMSSV);
        etName = findViewById(R.id.etName);
        etClass = findViewById(R.id.etClass);
        etSubject = findViewById(R.id.etSubject);
        ivStudent = findViewById(R.id.ivStudent);
        btnSave = findViewById(R.id.btnSave);
        currentEmail = getIntent().getStringExtra("currentUsername2");
        loadAboutInfo();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAboutInfo();
            }
        });
    }

    private void loadAboutInfo(){
        NNTDB dbHelper = new NNTDB(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + NNTDB.TABLE_ABOUT +  " WHERE " + NNTDB.COLUMN_USER_USERNAME +" = ? LIMIT 1";
        Cursor cursor = database.rawQuery(query,  new String[]{ currentEmail });
        if(cursor.moveToFirst()){
            try {
                String mssv = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_MSSV));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_FULLNAME_ABOUT));
                String className = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_CLASS));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_SUBJECT));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_IMAGE));

                etMSSV.setText(mssv);
                etName.setText(fullname);
                etClass.setText(className);
                etSubject.setText(subject);
                if (TextUtils.isEmpty(image)) {
                    image = "baseline_account_circle_24";
                }
                int imageRes = getResources().getIdentifier(image, "drawable", getPackageName());
                if(imageRes != 0){
                    ivStudent.setImageResource(imageRes);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi tải thông tin", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        database.close();
    }

    private void updateAboutInfo(){
        String mssv = etMSSV.getText().toString().trim();
        String fullname = etName.getText().toString().trim();
        String className = etClass.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String image = "baseline_account_circle_24";
        if(TextUtils.isEmpty(mssv) || TextUtils.isEmpty(fullname) ||
                TextUtils.isEmpty(className) || TextUtils.isEmpty(subject)){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        NNTDB dbHelper = new NNTDB(this);
        boolean result = dbHelper.updateAbout(currentEmail,mssv, fullname, className, subject, image);
        if(result){
            startActivity(new Intent(EditAbout.this, HomeActivity.class));
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}