//package com.meomeo.thachnnph50584_asm.Dao;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.meomeo.thachnnph50584_asm.DBHelper.NNTDB;
//import com.meomeo.thachnnph50584_asm.Model.Task;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TaskDAO {
//
//    private NNTDB dbHelper;
//
//    public TaskDAO(Context context) {
//        dbHelper = new NNTDB(context);
//    }
//
//    // Thêm công việc
//    public long addTask(Task task) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(NNTDB.COLUMN_TASK_NAME, task.getName());
//        values.put(NNTDB.COLUMN_TASK_CONTENT, task.getContent());
//        values.put(NNTDB.COLUMN_TASK_STATUS, task.getStatus());
//        values.put(NNTDB.COLUMN_TASK_START, task.getStart());
//        values.put(NNTDB.COLUMN_TASK_END, task.getEnd());
//        long result = db.insert(NNTDB.TABLE_TASK, null, values);
//        db.close();
//        return result;
//    }
//
//    // Lấy danh sách công việc
//    public List<Task> getAllTasks() {
//        List<Task> taskList = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query(NNTDB.TABLE_TASK, null, null, null, null, null, "id DESC");
//        if (cursor.moveToFirst()) {
//            do {
//                Task task = new Task();
//                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_ID)));
//                task.setName(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_NAME)));
//                task.setContent(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_CONTENT)));
//                task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_STATUS)));
//                task.setStart(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_START)));
//                task.setEnd(cursor.getString(cursor.getColumnIndexOrThrow(NNTDB.COLUMN_TASK_END)));
//                taskList.add(task);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return taskList;
//    }
//
//    // Cập nhật công việc
//    public int updateTask(Task task) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(NNTDB.COLUMN_TASK_NAME, task.getName());
//        values.put(NNTDB.COLUMN_TASK_CONTENT, task.getContent());
//        values.put(NNTDB.COLUMN_TASK_STATUS, task.getStatus());
//        values.put(NNTDB.COLUMN_TASK_START, task.getStart());
//        values.put(NNTDB.COLUMN_TASK_END, task.getEnd());
//        int result = db.update(NNTDB.TABLE_TASK, values, NNTDB.COLUMN_TASK_ID + "=?",
//                new String[]{String.valueOf(task.getId())});
//        db.close();
//        return result;
//    }
//
//    // Xóa công việc
//    public int deleteTask(int id) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        int result = db.delete(NNTDB.TABLE_TASK, NNTDB.COLUMN_TASK_ID + "=?",
//                new String[]{String.valueOf(id)});
//        db.close();
//        return result;
//    }
//}