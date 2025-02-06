package com.meomeo.thachnnph50584_asm.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String COLUMN_TASK_STATUS = "status";    // 0: Mới tạo, 1: Đang làm, 2: Hoàn thành, -1: Xóa
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

    private static final String DATABASE_NAME = "NNT_DB";
    private static final int DATABASE_VERSION = 4; // cập nhật phiên bản lên 2

    public NNTDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FULLNAME + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String INSERT_USERS = "INSERT INTO " + TABLE_USERS + " (" +
                COLUMN_USERNAME + ", " +
                COLUMN_EMAIL + ", " +
                COLUMN_PASSWORD + ", " +
                COLUMN_FULLNAME  +
                ") " +
                "VALUES ('thach', 'thach@gmail.com', 'thach', 'thach')";
        db.execSQL(INSERT_USERS);

        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_CONTENT + " TEXT, " +
                COLUMN_TASK_STATUS + " INTEGER, " +
                COLUMN_TASK_START + " TEXT, " +
                COLUMN_TASK_END + " TEXT)";
        db.execSQL(CREATE_TASK_TABLE);

        String CREATE_ABOUT_TABLE = "CREATE TABLE " + TABLE_ABOUT + " (" +
                COLUMN_ABOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MSSV + " TEXT, " +
                COLUMN_FULLNAME_ABOUT + " TEXT, " +
                COLUMN_CLASS + " TEXT, " +
                COLUMN_SUBJECT + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "))";
        db.execSQL(CREATE_ABOUT_TABLE);


        String INSERT_ABOUT = "INSERT INTO " + TABLE_ABOUT + " (" +
                COLUMN_MSSV + ", " +
                COLUMN_FULLNAME_ABOUT + ", " +
                COLUMN_CLASS + ", " +
                COLUMN_SUBJECT + ", " +
                COLUMN_IMAGE + ", " +
                COLUMN_USERNAME +
                ") " +
                "VALUES ('PH12345', 'Nguyễn Văn A', 'CNTT1', 'Lập trình Android', 'default_image_path', 'thach')";
        db.execSQL(INSERT_ABOUT);

    }

        // Cập nhật lại cấu trúc bảng khi DATABASE_VERSION tăng lên
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Thực hiện xóa các bảng cũ nếu tồn tại và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABOUT);
        onCreate(db);
    }

    // --- Các phương thức hỗ trợ xử lý dữ liệu ---

    // Kiểm tra xem có user nào trong bảng không
    public boolean hasUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    // Kiểm tra username và email có tồn tại không
    public boolean checkUser(String username, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Cập nhật mật khẩu cho user theo username
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }
    public boolean updateAbout(String mssv, String fullname, String className, String subject, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MSSV, mssv);
        values.put(COLUMN_FULLNAME_ABOUT, fullname);
        values.put(COLUMN_CLASS, className);
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_IMAGE, image);
        // Giả sử luôn cập nhật dòng có id = 1
        int rows = db.update(TABLE_ABOUT, values, COLUMN_ABOUT_ID + " = ?", new String[]{"1"});
        db.close();
        return rows > 0;
    }
}
