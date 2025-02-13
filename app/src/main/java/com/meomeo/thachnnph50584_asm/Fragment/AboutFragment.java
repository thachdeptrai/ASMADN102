package com.meomeo.thachnnph50584_asm.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
import com.meomeo.thachnnph50584_asm.Activity.EditAbout;
import com.meomeo.thachnnph50584_asm.R;

public class AboutFragment extends Fragment {

    private TextView User ,tvMSSV, tvName, tvClass, tvSubject;
    private ImageView ivStudent;
    private Button btnEditInfo;
    private String currentEmail;
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        User  = view.findViewById(R.id.User);
        tvMSSV = view.findViewById(R.id.tvMSSV);
        tvName = view.findViewById(R.id.tvName);
        tvClass = view.findViewById(R.id.tvClass);
        tvSubject = view.findViewById(R.id.tvSubject);
        ivStudent = view.findViewById(R.id.ivStudent);
        btnEditInfo = view.findViewById(R.id.btnEditInfo);
        currentEmail = getActivity().getIntent().getStringExtra("currentUsername");
        loadAboutInfo();

        btnEditInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Mở Activity sửa thông tin
                Intent intent = new Intent(getActivity(), EditAbout.class);
                intent.putExtra("currentUsername2", currentEmail);

                startActivity(intent);
            }
        });

        return view;
    }

    private void loadAboutInfo(){
        NNTDB dbHelper = new NNTDB(getContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + NNTDB.TABLE_ABOUT +
                " WHERE " + NNTDB.COLUMN_USER_USERNAME + " = ? LIMIT 1";
        Cursor cursor = database.rawQuery(query, new String[]{ currentEmail });

        String query2 = "SELECT * FROM " + NNTDB.TABLE_USERS + " WHERE " + NNTDB.COLUMN_USERNAME + " = ? LIMIT 1";
        Cursor cursor2 = database.rawQuery(query2, new String[]{ currentEmail });
        if(cursor2.moveToFirst()) {
            try {
                String username = cursor2.getString(cursor2.getColumnIndexOrThrow(NNTDB.COLUMN_USERNAME));
                User.setText("Username: "+ username);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi tải Username", Toast.LENGTH_SHORT).show();
            }
            cursor2.close();

        }
        if(cursor.moveToFirst()){
            try {
                String mssv = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_MSSV));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_FULLNAME_ABOUT));
                String className = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_CLASS));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_SUBJECT));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_IMAGE));
                tvMSSV.setText("MSSV: " + mssv);
                tvName.setText("Họ tên: " + fullname);
                tvClass.setText("Lớp: " + className);
                tvSubject.setText("Môn học: " + subject);

               int imageRes = 0;
                if(image != null && !image.trim().isEmpty()){
                    imageRes = getResources().getIdentifier(image, "drawable", getContext().getPackageName());
                }
                if(imageRes == 0){
                    imageRes = R.drawable.baseline_account_circle_24; // hoặc ảnh mặc định khác
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi tải thông tin", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        database.close();
    }
}
