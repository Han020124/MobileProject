package com.example.mobileproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "timetable.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "subjects";
    private static final String COL_ID = "id";
    private static final String COL_SUBJECT = "subject";
    private static final String COL_DAY = "day";
    private static final String COL_START = "start_period";
    private static final String COL_DURATION = "duration";
    private static final String COL_COLOR = "color";

    public TDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUBJECT + " TEXT, " +
                COL_DAY + " TEXT, " +
                COL_START + " INTEGER, " +
                COL_DURATION + " INTEGER, " +
                COL_COLOR + " TEXT)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addSubject(TimetableItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SUBJECT, item.getSubjectName());
        values.put(COL_DAY, item.getDay());
        values.put(COL_START, item.getStartPeriod());
        values.put(COL_DURATION, item.getDuration());
        values.put(COL_COLOR, item.getColor());
        return db.insert(TABLE_NAME, null, values);
    }

    public int deleteSubject(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<TimetableItem> getAllSubjects() {
        List<TimetableItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBJECT));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COL_DAY));
                int start = cursor.getInt(cursor.getColumnIndexOrThrow(COL_START));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DURATION));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(COL_COLOR));
                list.add(new TimetableItem(id, subject, day, start, duration, color));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
