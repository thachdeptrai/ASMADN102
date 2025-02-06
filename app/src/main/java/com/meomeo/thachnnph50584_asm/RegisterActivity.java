package com.meomeo.thachnnph50584_asm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput, emailInput, passwordInput, fullnameInput;
    private Button registerButton , login;
    private NNTDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new NNTDB(this);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        fullnameInput = findViewById(R.id.fullnameInput);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);
        registerButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String fullname = fullnameInput.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("email", email);
                values.put("password", password);
                values.put("fullname", fullname);

                long result = database.insert("users", null, values);
                if (result != -1) {
                    ContentValues aboutValues = new ContentValues();
                    aboutValues.put("user_username", username);
                    // aboutValues.put("mssv", "");
                    // aboutValues.put("fullname_about", "");
                    // aboutValues.put("class_name", "");
                    // aboutValues.put("subject", "");
                    // aboutValues.put("image", "");

                    long aboutResult = database.insert("about", null, aboutValues);
                    if (aboutResult == -1) {
                        Toast.makeText(this, "Đăng ký thành công, nhưng lưu thông tin About thất bại", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
login.setOnClickListener(view ->{
    startActivity(new Intent(this,LoginActivity.class));
    finish();
});
    }
}
