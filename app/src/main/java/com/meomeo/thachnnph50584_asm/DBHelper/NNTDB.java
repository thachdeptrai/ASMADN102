package com.meomeo.thachnnph50584_asm.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meomeo.thachnnph50584_asm.Model.Task;

public class NNTDB extends SQLiteOpenHelper {

    // --- Bảng USERS ---
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FULLNAME = "fullname";

    // --- Bảng TASK ---
    public static final String TABLE_TASK = "task";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_NAME = "name";
    public static final String COLUMN_TASK_CONTENT = "content";
    public static final String COLUMN_TASK_STATUS = "status";    // 0: mới tạo, 1: đang làm, 2: hoàn thành, -1: xóa
    public static final String COLUMN_TASK_START = "start_date";
    public static final String COLUMN_TASK_END = "end_date";

    // --- Bảng ABOUT ---
    public static final String TABLE_ABOUT = "about";
    public static final String COLUMN_ABOUT_ID = "id";
    public static final String COLUMN_MSSV = "mssv";
    public static final String COLUMN_FULLNAME_ABOUT = "fullname_about";
    public static final String COLUMN_CLASS = "class_name";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_IMAGE = "image";
    // Cột khóa ngoại để liên kết với bảng USERS (dựa vào username)
    public static final String COLUMN_USER_USERNAME = "user_username";

    private static final String DATABASE_NAME = "NNT_DB";
    private static final int DATABASE_VERSION = 8; // Tăng version để cập nhật schema

    public NNTDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng USERS
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FULLNAME + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert 1 tài khoản mẫu vào USERS
        String INSERT_USERS = "INSERT INTO " + TABLE_USERS + " (" +
                COLUMN_USERNAME + ", " +
                COLUMN_EMAIL + ", " +
                COLUMN_PASSWORD + ", " +
                COLUMN_FULLNAME +
                ") VALUES ('thach', 'thach@gmail.com', 'thach', 'thach')";
        db.execSQL(INSERT_USERS);

        // Tạo bảng TASK
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_CONTENT + " TEXT, " +
                COLUMN_TASK_STATUS + " INTEGER, " +
                COLUMN_TASK_START + " TEXT, " +
                COLUMN_TASK_END + " TEXT)";
        db.execSQL(CREATE_TASK_TABLE);

        // Tạo bảng ABOUT với cột khóa ngoại user_username tham chiếu đến users(username)
        String CREATE_ABOUT_TABLE = "CREATE TABLE " + TABLE_ABOUT + " (" +
                COLUMN_ABOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MSSV + " TEXT, " +
                COLUMN_FULLNAME_ABOUT + " TEXT, " +
                COLUMN_CLASS + " TEXT, " +
                COLUMN_SUBJECT + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_USER_USERNAME + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USER_USERNAME + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "))";
        db.execSQL(CREATE_ABOUT_TABLE);

        String INSERT_ABOUT = "INSERT INTO " + TABLE_ABOUT + " (" +
                COLUMN_USER_USERNAME + ", " +
                COLUMN_MSSV + ", " +
                COLUMN_FULLNAME_ABOUT + ", " +
                COLUMN_CLASS + ", " +
                COLUMN_SUBJECT + ", " +
                COLUMN_IMAGE +
                ") SELECT " + COLUMN_USERNAME + ", NULL, NULL, NULL, NULL, NULL " +
                "FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = 'thach'";
        db.execSQL(INSERT_ABOUT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại bảng mới
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABOUT);
        onCreate(db);
    }

    public boolean hasUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public boolean checkUser(String username, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }

    public boolean updateAbout(String username, String mssv, String fullname, String className, String subject, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MSSV, mssv);
        values.put(COLUMN_FULLNAME_ABOUT, fullname);
        values.put(COLUMN_CLASS, className);
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_IMAGE, image);
        int rows = db.update(TABLE_ABOUT, values, COLUMN_USER_USERNAME + " = ?", new String[]{ username });
        db.close();
        return rows > 0;
    }
    // Phương thức thêm task
    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getName());
        values.put(COLUMN_TASK_CONTENT, task.getContent());
        values.put(COLUMN_TASK_STATUS, task.getStatus());
        values.put(COLUMN_TASK_START, task.getStart());
        values.put(COLUMN_TASK_END, task.getEnd());
        long id = db.insert(TABLE_TASK, null, values);
        db.close();
        return id;
    }
    // Phương thức cập nhật task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getName());
        values.put(COLUMN_TASK_CONTENT, task.getContent());
        values.put(COLUMN_TASK_STATUS, task.getStatus());
        values.put(COLUMN_TASK_START, task.getStart());
        values.put(COLUMN_TASK_END, task.getEnd());
        int rows = db.update(TABLE_TASK, values, COLUMN_TASK_ID + "=?", new String[]{ String.valueOf(task.getId()) });
        db.close();
        return rows;
    }
    // Phương thức xóa task
    public int deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_TASK, COLUMN_TASK_ID + "=?", new String[]{ String.valueOf(taskId) });
        db.close();
        return rows;
    }
}
