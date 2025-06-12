package com.example.mobileproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {

    private Spinner spinnerSubjects;
    private Button btnAdd, btnDel;
    private ImageView imageTimetable;
    private ImageButton Btnarrow;

    private TDBHelper dbHelper;
    private List<TimetableItem> currentSubjects;

    private static class SubjectInfo {
        String name;
        String day;
        int start;
        int duration;
        String color;

        SubjectInfo(String name, String day, int start, int duration, String color) {
            this.name = name;
            this.day = day;
            this.start = start;
            this.duration = duration;
            this.color = color;
        }
    }

    private static final SubjectInfo[] ALL_SUBJECTS = new SubjectInfo[]{
            new SubjectInfo("소프트웨어 공학 월(1-3)", "월", 1, 3, "#E17055"),
            new SubjectInfo("소프트웨어 공학 월(4-6)", "월", 4, 3, "#E17055"),
            new SubjectInfo("웹응용기술 화(4-6)", "화", 4, 3, "#6C5CE7"),
            new SubjectInfo("웹응용기술 화(7-9)", "화", 7, 3, "#6C5CE7"),
            new SubjectInfo("모바일프로그래밍 화(4-6)", "화", 4, 3, "#A29BFE"),
            new SubjectInfo("모바일프로그래밍 화(7-9)", "화", 7, 3, "#A29BFE"),
            new SubjectInfo("인공지능 목(4-6)", "목", 4, 3, "#FD79A8"),
            new SubjectInfo("인공지능 목(7-9)", "목", 7, 3, "#FD79A8"),
            new SubjectInfo("컴퓨터알고리즘 목(4-6)", "목", 4, 3, "#FDCB6E"),
            new SubjectInfo("컴퓨터알고리즘 목(7-9)", "목", 7, 3, "#FDCB6E")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        dbHelper = new TDBHelper(this);

        spinnerSubjects = findViewById(R.id.spinner_subjects);
        btnAdd = findViewById(R.id.btn_add);
        btnDel = findViewById(R.id.btn_del);
        imageTimetable = findViewById(R.id.image_timetable);
        Btnarrow = findViewById(R.id.btn_arrow);

        Btnarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 스피너에 과목명 세팅
        List<String> subjectNames = new ArrayList<>();
        for (SubjectInfo s : ALL_SUBJECTS) {
            subjectNames.add(s.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

        loadSubjectsFromDB();
        generateTimetableImage();

        btnAdd.setOnClickListener(v -> {
            int pos = spinnerSubjects.getSelectedItemPosition();
            SubjectInfo selected = ALL_SUBJECTS[pos];

            boolean exists = false;
            for (TimetableItem item : currentSubjects) {
                if (item.getSubjectName().equals(selected.name)
                        && item.getDay().equals(selected.day)
                        && item.getStartPeriod() == selected.start) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                Toast.makeText(this, "이미 추가된 과목입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            TimetableItem newItem = new TimetableItem(
                    0,
                    selected.name,
                    selected.day,
                    selected.start,
                    selected.duration,
                    selected.color);

            long id = dbHelper.addSubject(newItem);
            if (id != -1) {
                loadSubjectsFromDB();
                generateTimetableImage();
                Toast.makeText(this, "과목 추가 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "과목 추가 실패", Toast.LENGTH_SHORT).show();
            }
        });

        btnDel.setOnClickListener(v -> {
            int pos = spinnerSubjects.getSelectedItemPosition();
            SubjectInfo selected = ALL_SUBJECTS[pos];

            TimetableItem toDelete = null;
            for (TimetableItem item : currentSubjects) {
                if (item.getSubjectName().equals(selected.name)
                        && item.getDay().equals(selected.day)
                        && item.getStartPeriod() == selected.start) {
                    toDelete = item;
                    break;
                }
            }

            if (toDelete == null) {
                Toast.makeText(this, "삭제할 과목이 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            int count = dbHelper.deleteSubject(toDelete.getId());
            if (count > 0) {
                loadSubjectsFromDB();
                generateTimetableImage();
                Toast.makeText(this, "과목 삭제 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "과목 삭제 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSubjectsFromDB() {
        currentSubjects = dbHelper.getAllSubjects();
    }

    private void generateTimetableImage() {
        int width = 800;
        int height = 1200;
        int leftMargin = 80;
        int topMargin = 80;
        int cellWidth = 120;
        int cellHeight = 120;
        int periods = 9;
        String[] days = {"월", "화", "수", "목", "금"};

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(40);
        paint.setColor(Color.BLACK);

        for (int i = 0; i < days.length; i++) {
            float x = leftMargin + cellWidth * i + 10;
            float y = topMargin - 20;
            canvas.drawText(days[i], x, y, paint);
        }

        for (int i = 1; i <= periods; i++) {
            float x = leftMargin - 60;
            float y = topMargin + cellHeight * (i - 1) + 80;
            canvas.drawText(i + "교시", x, y, paint);
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        for (int d = 0; d < days.length; d++) {
            for (int p = 0; p < periods; p++) {
                int left = leftMargin + d * cellWidth;
                int top = topMargin + p * cellHeight;
                int right = left + cellWidth;
                int bottom = top + cellHeight;
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }

        paint.setStyle(Paint.Style.FILL);
        for (TimetableItem item : currentSubjects) {
            int dayIndex = -1;
            for (int i = 0; i < days.length; i++) {
                if (days[i].equals(item.getDay())) {
                    dayIndex = i;
                    break;
                }
            }
            if (dayIndex == -1) continue;

            paint.setColor(Color.parseColor(item.getColor()));

            int left = leftMargin + dayIndex * cellWidth + 2;
            int top = topMargin + (item.getStartPeriod() - 1) * cellHeight + 2;
            int right = left + cellWidth - 4;
            int bottom = top + cellHeight * item.getDuration() - 4;

            canvas.drawRect(left, top, right, bottom, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(30);
            paint.setAntiAlias(true);

            float textX = left + 10;
            float textY = top + 40;

            String[] lines = item.getSubjectName().split("\\(");
            String firstLine = lines[0];
            String secondLine = (lines.length > 1) ? "(" + lines[1] : "";

            canvas.drawText(firstLine, textX, textY, paint);
            canvas.drawText(secondLine, textX, textY + 35, paint);
        }

        imageTimetable.setImageBitmap(bitmap);
    }
}
