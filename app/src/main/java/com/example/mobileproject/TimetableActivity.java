package com.example.teamproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {

    private ImageButton btnArrowHeader;
    private ImageView imageTimetable;

    private List<TimetableItem> timetableItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        initializeViews();
        initializeTimetableData();
        setupClickListeners();
        generateTimetableImage();
    }

    private void initializeViews() {
        btnArrowHeader = findViewById(R.id.btn_arrow_header);
        imageTimetable = findViewById(R.id.image_timetable);
    }

    private void initializeTimetableData() {
        timetableItems = new ArrayList<>();

        timetableItems.add(new TimetableItem("자바프로그래밍", "월", 9, 2, "#FF6B6B"));
        timetableItems.add(new TimetableItem("소프트웨어공학", "화", 10, 2, "#4ECDC4"));
        timetableItems.add(new TimetableItem("컴퓨터알고리즘", "수", 13, 2, "#45B7D1"));
        timetableItems.add(new TimetableItem("인공지능", "목", 14, 2, "#96CEB4"));
        timetableItems.add(new TimetableItem("모바일프로그래밍", "금", 11, 2, "#FFEAA7"));
    }

    private void setupClickListeners() {
        btnArrowHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addNewSubject() {
        String[] sampleSubjects = {"데이터베이스", "네트워크", "운영체제", "웹프로그래밍", "게임개발"};
        String[] days = {"월", "화", "수", "목", "금"};
        String[] colors = {"#E17055", "#6C5CE7", "#A29BFE", "#FD79A8", "#FDCB6E"};

        if (timetableItems.size() < 10) { // 최대 10개 과목까지만
            int index = (int) (Math.random() * sampleSubjects.length);
            String subject = sampleSubjects[index];
            String day = days[(int) (Math.random() * days.length)];
            int startTime = 9 + (int) (Math.random() * 8); // 9시~16시 사이
            String color = colors[index % colors.length];

            TimetableItem newItem = new TimetableItem(subject, day, startTime, 1, color);
            timetableItems.add(newItem);

            generateTimetableImage();

            Toast.makeText(this, subject + " 과목이 추가되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "더 이상 과목을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateTimetableImage() {
        imageTimetable.post(new Runnable() {
            @Override
            public void run() {
                int width = imageTimetable.getWidth();
                int height = imageTimetable.getHeight();

                if (width < 100) width = 1000;
                if (height < 100) height = 800;

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);

                canvas.drawColor(Color.WHITE);

                Paint paint = new Paint();
                paint.setAntiAlias(true);

                drawTimetableGrid(canvas, paint, width, height);

                drawTimetableItems(canvas, paint, width, height);

                imageTimetable.setImageBitmap(bitmap);
            }
        });
    }

    private void drawTimetableGrid(Canvas canvas, Paint paint, int width, int height) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);

        int dayWidth = width / 6;
        for (int i = 0; i <= 6; i++) {
            canvas.drawLine(i * dayWidth, 0, i * dayWidth, height, paint);
        }

        int timeHeight = height / 11;
        for (int i = 0; i <= 10; i++) {
            canvas.drawLine(0, i * timeHeight, width, i * timeHeight, paint);
        }

        paint.setColor(Color.parseColor("#E8E8E8"));
        canvas.drawRect(0, 0, width, timeHeight, paint);
        canvas.drawRect(0, 0, dayWidth, height, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(Math.min(width, height) / 25);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);

        String[] days = {"시간", "월", "화", "수", "목", "금"};
        for (int i = 0; i < days.length; i++) {
            canvas.drawText(days[i], i * dayWidth + dayWidth/2, timeHeight/2 + paint.getTextSize()/3, paint);
        }

        paint.setTextSize(Math.min(width, height) / 30);
        for (int i = 0; i < 10; i++) {
            String time = (9 + i) + ":00";
            canvas.drawText(time, dayWidth/2, (i + 1) * timeHeight + timeHeight/2 + paint.getTextSize()/3, paint);
        }
    }

    private void drawTimetableItems(Canvas canvas, Paint paint, int width, int height) {
        int dayWidth = width / 6;
        int timeHeight = height / 11;

        for (TimetableItem item : timetableItems) {
            int dayIndex = getDayIndex(item.getDay());
            if (dayIndex > 0) {
                int left = dayIndex * dayWidth + 8;
                int top = (item.getStartTime() - 8) * timeHeight + 8;
                int right = left + dayWidth - 16;
                int bottom = top + (item.getDuration() * timeHeight) - 16;

                paint.setColor(Color.parseColor(item.getColor()));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(left, top, right, bottom, paint);

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                canvas.drawRect(left, top, right, bottom, paint);

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(Math.min(dayWidth, timeHeight) / 4);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);

                String text = item.getSubjectName();

                if (text.length() > 6) {
                    String[] words = text.split("(?<=\\G.{" + (text.length()/2) + "})");
                    if (words.length >= 2) {
                        int textX = left + (right - left) / 2;
                        int textY1 = top + (bottom - top) / 2 - (int)(paint.getTextSize() / 2);
                        int textY2 = top + (bottom - top) / 2 + (int)(paint.getTextSize() / 2);

                        canvas.drawText(words[0], textX, textY1, paint);
                        canvas.drawText(words[1], textX, textY2, paint);
                    } else {
                        int textX = left + (right - left) / 2;
                        int textY = top + (bottom - top) / 2 + (int)(paint.getTextSize() / 3);
                        canvas.drawText(text, textX, textY, paint);
                    }
                } else {
                    int textX = left + (right - left) / 2;
                    int textY = top + (bottom - top) / 2 + (int)(paint.getTextSize() / 3);
                    canvas.drawText(text, textX, textY, paint);
                }
            }
        }
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "월": return 1;
            case "화": return 2;
            case "수": return 3;
            case "목": return 4;
            case "금": return 5;
            default: return 0;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TimetableActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private static class TimetableItem {
        private String subjectName;
        private String day;
        private int startTime;
        private int duration;
        private String color;

        public TimetableItem(String subjectName, String day, int startTime, int duration, String color) {
            this.subjectName = subjectName;
            this.day = day;
            this.startTime = startTime;
            this.duration = duration;
            this.color = color;
        }

        public String getSubjectName() { return subjectName; }
        public String getDay() { return day; }
        public int getStartTime() { return startTime; }
        public int getDuration() { return duration; }
        public String getColor() { return color; }
    }
}