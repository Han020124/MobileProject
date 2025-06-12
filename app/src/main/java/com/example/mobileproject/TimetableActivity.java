package com.example.mobileproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;

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

    private TimetableItem selectedItem = null;  // 클릭해서 선택된 과목 저장

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        dbHelper = new TDBHelper(this);

        spinnerSubjects = findViewById(R.id.spinner_subjects);
        btnAdd = findViewById(R.id.btn_add);
        btnDel = findViewById(R.id.btn_del);
        imageTimetable = findViewById(R.id.image_timetable);
        Btnarrow = findViewById(R.id.btn_arrow);

        Btnarrow.setOnClickListener(v -> {
            Intent intent = new Intent(TimetableActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
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

            // 이미 추가된 과목인지 검사
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
                scheduleSubjectNotification(selected);
                loadSubjectsFromDB();
                generateTimetableImage();
                Toast.makeText(this, "과목 추가 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "과목 추가 실패", Toast.LENGTH_SHORT).show();
            }
        });

        // 시간표 이미지에서 과목 선택 처리
        imageTimetable.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                Bitmap bitmap = ((BitmapDrawable)imageTimetable.getDrawable()).getBitmap();

                float scaleX = (float) bitmap.getWidth() / imageTimetable.getWidth();
                float scaleY = (float) bitmap.getHeight() / imageTimetable.getHeight();

                float bitmapX = x * scaleX;
                float bitmapY = y * scaleY;

                TimetableItem clicked = findItemByCoordinates(bitmapX, bitmapY);
                if (clicked != null) {
                    selectedItem = clicked;
                    Toast.makeText(this, selectedItem.getSubjectName() + " 선택됨", Toast.LENGTH_SHORT).show();
                    generateTimetableImage();  // 선택 강조 갱신
                } else {
                    selectedItem = null;
                    Toast.makeText(this, "과목 영역을 클릭하세요", Toast.LENGTH_SHORT).show();
                    generateTimetableImage();
                }
                return true;
            }
            return false;
        });


        // 삭제 버튼 클릭 시 선택된 과목 삭제
        btnDel.setOnClickListener(v -> {
            if (selectedItem == null) {
                Toast.makeText(this, "삭제할 과목을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            int count = dbHelper.deleteSubject(selectedItem.getId());
            if (count > 0) {
                Toast.makeText(this, "과목 삭제 완료", Toast.LENGTH_SHORT).show();
                selectedItem = null;
                loadSubjectsFromDB();
                generateTimetableImage();
            } else {
                Toast.makeText(this, "과목 삭제 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleSubjectNotification(SubjectInfo subject) {
        Calendar calendar = Calendar.getInstance();

        int dayOfWeek;
        switch (subject.day) {
            case "월":
                dayOfWeek = Calendar.MONDAY;
                break;
            case "화":
                dayOfWeek = Calendar.TUESDAY;
                break;
            case "수":
                dayOfWeek = Calendar.WEDNESDAY;
                break;
            case "목":
                dayOfWeek = Calendar.THURSDAY;
                break;
            case "금":
                dayOfWeek = Calendar.FRIDAY;
                break;
            default:
                dayOfWeek = -1;
                break;
        }

        if (dayOfWeek == -1) return;

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 9 + subject.start - 1); // 예: 1교시 = 9시
        calendar.set(Calendar.MINUTE, 55); // 5분 전
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1); // 이미 지난 경우 다음 주 예약
        }

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("subject", subject.name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                subject.name.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    private void loadSubjectsFromDB() {
        currentSubjects = dbHelper.getAllSubjects();
    }

    // 클릭 좌표로 과목 찾기
    private TimetableItem findItemByCoordinates(float x, float y) {
        int leftMargin = 80;
        int topMargin = 80;
        int cellWidth = 120;
        int cellHeight = 120;
        String[] days = {"월", "화", "수", "목", "금"};

        int dayIndex = (int) ((x - leftMargin) / cellWidth);
        if (dayIndex < 0 || dayIndex >= days.length) return null;

        int periodIndex = (int) ((y - topMargin) / cellHeight);
        if (periodIndex < 0 || periodIndex >= 9) return null;

        String clickedDay = days[dayIndex];
        int clickedPeriod = periodIndex + 1;

        for (TimetableItem item : currentSubjects) {
            if (item.getDay().equals(clickedDay)) {
                int start = item.getStartPeriod();
                int end = start + item.getDuration() - 1;
                if (clickedPeriod >= start && clickedPeriod <= end) {
                    return item;
                }
            }
        }
        return null;
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

        // 요일 텍스트
        for (int i = 0; i < days.length; i++) {
            float x = leftMargin + cellWidth * i + 10;
            float y = topMargin - 20;
            canvas.drawText(days[i], x, y, paint);
        }

        // 교시 텍스트
        for (int i = 1; i <= periods; i++) {
            float x = leftMargin - 60;
            float y = topMargin + cellHeight * (i - 1) + 80;
            canvas.drawText(i + "교시", x, y, paint);
        }

        // 그리드 그리기
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

        // 과목 칸 그리기
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

            int left = leftMargin + dayIndex * cellWidth + 2;
            int top = topMargin + (item.getStartPeriod() - 1) * cellHeight + 2;
            int right = left + cellWidth - 4;
            int bottom = top + cellHeight * item.getDuration() - 4;

            if (selectedItem != null && selectedItem.getId() == item.getId()) {
                // 선택된 과목 강조: 노란색 테두리
                paint.setColor(Color.parseColor(item.getColor()));
                canvas.drawRect(left, top, right, bottom, paint);

                Paint borderPaint = new Paint();
                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setColor(Color.YELLOW);
                borderPaint.setStrokeWidth(8);
                canvas.drawRect(left, top, right, bottom, borderPaint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(36);
                paint.setAntiAlias(true);
                canvas.drawText(item.getSubjectName(), left + 10, top + 60, paint);

            } else {
                // 일반 과목 칸
                paint.setColor(Color.parseColor(item.getColor()));
                canvas.drawRect(left, top, right, bottom, paint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(36);
                paint.setAntiAlias(true);
                canvas.drawText(item.getSubjectName(), left + 10, top + 60, paint);
            }
        }

        imageTimetable.setImageBitmap(bitmap);
    }
}
