package com.meomeo.thachnnph50584_asm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NNTDB db = new NNTDB(this);

        if (db.hasUser()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, RegisterActivity.class));
        }
        finish();
    }

}