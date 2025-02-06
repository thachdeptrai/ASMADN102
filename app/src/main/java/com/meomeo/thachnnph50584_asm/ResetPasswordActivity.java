package com.meomeo.thachnnph50584_asm;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button updatePasswordButton;
    private NNTDB dbHelper;
    private String username; // Lấy từ ForgotPasswordActivity thông qua Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);
        dbHelper = new NNTDB(this);

        // Lấy username từ Intent
        username = getIntent().getStringExtra("username");

        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật mật khẩu mới vào CSDL
                boolean updated = dbHelper.updatePassword(username, newPassword);
                if (updated) {
                    Toast.makeText(this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this , LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}