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
import com.meomeo.thachnnph50584_asm.EditAbout;
import com.meomeo.thachnnph50584_asm.R;

public class AboutFragment extends Fragment {

    private TextView User ,tvMSSV, tvName, tvClass, tvSubject;
    private ImageView ivStudent;
    private Button btnEditInfo;

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

        loadAboutInfo();

        btnEditInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Mở Activity sửa thông tin
                Intent intent = new Intent(getActivity(), EditAbout.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadAboutInfo(){
        NNTDB dbHelper = new NNTDB(getContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, u." + NNTDB.COLUMN_USERNAME +
                " FROM " + NNTDB.TABLE_ABOUT + " AS a " +
                "INNER JOIN " + NNTDB.TABLE_USERS + " AS u " +
                "ON a.user_username = u." + NNTDB.COLUMN_USERNAME +
                " LIMIT 1";

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            try {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_USERNAME));
                String mssv = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_MSSV));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_FULLNAME_ABOUT));
                String className = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_CLASS));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_SUBJECT));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_IMAGE));
                User.setText("Username: "+ username);
                tvMSSV.setText("MSSV: " + mssv);
                tvName.setText("Họ tên: " + fullname);
                tvClass.setText("Lớp: " + className);
                tvSubject.setText("Môn học: " + subject);

                int imageRes = getResources().getIdentifier(image, "drawable", getContext().getPackageName());
                if(imageRes != 0){
                    ivStudent.setImageResource(imageRes);
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
