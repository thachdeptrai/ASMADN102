package com.meomeo.thachnnph50584_asm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.R;

public class forgot_password_activity extends AppCompatActivity {
    private EditText usernameInput, emailInput;
    private Button verifyButton;
    private NNTDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        verifyButton = findViewById(R.id.verifyButton);
        dbHelper = new NNTDB(this);

        verifyButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                // Kiểm tra thông tin người dùng
                if (dbHelper.checkUser(username, email)) {
                    // Nếu thông tin khớp, chuyển sang màn hình tạo mật khẩu mới
                    Intent intent = new Intent(forgot_password_activity.this, ResetPasswordActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Username và Email không khớp!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}