package com.meomeo.thachnnph50584_asm.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.R;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameI, passwordInput;
    private Button loginButton,registerButton;
    private NNTDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new NNTDB(this);
        usernameI = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);
        forgotPasswordText.setOnClickListener(view -> {
             Intent intent = new Intent(this, forgot_password_activity.class);
             startActivity(intent);
        });
        loginButton.setOnClickListener(view -> {
            String username = usernameI.getText().toString();
            String password = passwordInput.getText().toString();

            SQLiteDatabase database = db.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                // Chuyển đến màn hình chính
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                intent.putExtra("currentUsername", username);

                startActivity(intent);

            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        });
        registerButton.setOnClickListener(view ->{
            startActivity(new Intent(this,RegisterActivity.class));

        });
    }
}