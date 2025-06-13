package com.example.mobileproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "subjectsDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "subjects";
    private static final String COL_NAME = "name";         // 과목명
    private static final String COL_CREDIT = "credit";     // 학점
    private static final String COL_CATEGORY = "category"; // 이수 구분

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // DB 생성 시 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_NAME + " TEXT PRIMARY KEY, " +
                COL_CREDIT + " INTEGER, " +
                COL_CATEGORY + " TEXT)";
        db.execSQL(createTableSQL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 새 과목 추가
    public boolean insertSubject(String name, int credit, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_CREDIT, credit);
        values.put(COL_CATEGORY, category);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // 과목 조회
    public Cursor getAllSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // 과목 수정
    public boolean updateSubject(String oldName, String newName, int credit, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newName);
        values.put(COL_CREDIT, credit);
        values.put(COL_CATEGORY, category);

        int rows = db.update(TABLE_NAME, values, COL_NAME + "=?", new String[]{oldName});
        return rows > 0;
    }

    // 과목 삭제
    public boolean deleteSubject(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, COL_NAME + "=?", new String[]{name});
        return rows > 0;
    }

    // 과목 하나 조회
    public Cursor getSubjectByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_NAME + "=?", new String[]{name});
    }


    public HashMap<String, Integer> getCategoryCreditSum() {
        HashMap<String, Integer> result = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COL_CATEGORY + ", SUM(" + COL_CREDIT + ") " +
                "FROM " + TABLE_NAME + " GROUP BY " + COL_CATEGORY, null);

        while (cursor.moveToNext()) {
            String category = cursor.getString(0);
            int totalCredit = cursor.getInt(1);
            result.put(category, totalCredit);
        }

        cursor.close();
        return result;
    }
}
