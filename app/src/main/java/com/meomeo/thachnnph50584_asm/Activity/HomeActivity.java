package com.meomeo.thachnnph50584_asm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.meomeo.thachnnph50584_asm.Fragment.AboutFragment;
import com.meomeo.thachnnph50584_asm.Fragment.TaskManagerFragment;
import com.meomeo.thachnnph50584_asm.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnTaskManager, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnTaskManager = findViewById(R.id.btnTaskManager);
        btnAbout = findViewById(R.id.btnAbout);

        // Hiển thị mặc định fragment quản lý công việc
        loadFragment(new TaskManagerFragment());

        btnTaskManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TaskManagerFragment());
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AboutFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // Thay thế fragment trong container
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
