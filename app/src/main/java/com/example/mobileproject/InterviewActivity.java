package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InterviewActivity extends AppCompatActivity {

    private Spinner spinnerProfessor, spinnerDate;
    private Button buttonApply;
    private Button[] timeButtons;
    private String selectedTime = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerProfessor = findViewById(R.id.spinner_professor);
        spinnerDate = findViewById(R.id.spinner_date);
        buttonApply = findViewById(R.id.button_apply);
        ImageButton btnArrow = findViewById(R.id.btn_arrow);

        timeButtons = new Button[]{
                findViewById(R.id.btn_0900),
                findViewById(R.id.btn_0930),
                findViewById(R.id.btn_1000),
                findViewById(R.id.btn_1030),
                findViewById(R.id.btn_1100),
                findViewById(R.id.btn_1130),
                findViewById(R.id.btn_1300),
                findViewById(R.id.btn_1330),
                findViewById(R.id.btn_1400),
                findViewById(R.id.btn_1430),
                findViewById(R.id.btn_1500),
                findViewById(R.id.btn_1530),
                findViewById(R.id.btn_1530_2),
                findViewById(R.id.btn_1600),
                findViewById(R.id.btn_1630),
                findViewById(R.id.btn_1700),
                findViewById(R.id.btn_1730),
                findViewById(R.id.btn_1800)
        };

        ArrayList<String> professors = new ArrayList<>();
        professors.add("교수님 A");
        professors.add("교수님 B");
        professors.add("교수님 C");
        ArrayAdapter<String> adapterProf = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, professors);
        adapterProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfessor.setAdapter(adapterProf);

        ArrayList<String> dates = new ArrayList<>();
        dates.add("2025-06-05");
        dates.add("2025-06-06");
        dates.add("2025-06-07");
        ArrayAdapter<String> adapterDate = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapterDate);

        for (Button timeBtn : timeButtons) {
            timeBtn.setOnClickListener(v -> selectTimeButton((Button) v));
        }

        buttonApply.setOnClickListener(v -> applyConsultation());

        btnArrow.setOnClickListener(v -> finish());
    }

    private void selectTimeButton(Button clickedButton) {
        for (Button btn : timeButtons) {
            btn.setBackgroundResource(R.drawable.button_time_background);
            btn.setTextColor(getResources().getColor(android.R.color.black));
        }
        clickedButton.setBackgroundResource(R.drawable.button_time_selected_background);
        clickedButton.setTextColor(getResources().getColor(android.R.color.white));
        selectedTime = clickedButton.getText().toString();
    }

    private void applyConsultation() {
        String professor = (String) spinnerProfessor.getSelectedItem();
        String date = (String) spinnerDate.getSelectedItem();
        String studentName = "홍길동"; // 실제 앱에서는 로그인 정보 등에서 가져오기

        if (professor == null || date == null || selectedTime == null) {
            Toast.makeText(this, "교수님, 날짜, 시간을 모두 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 교수 이름에 따른 이메일 매핑
        String professorEmail = professor.equals("교수님 A") ? "han3003ty@gmail.com" :
                professor.equals("교수님 B") ? "professorB@example.com" :
                        "professorC@example.com";

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/consultation/apply");  // 에뮬레이터 기준 localhost 주소
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);

                String jsonInputString = "{"
                        + "\"professorEmail\":\"" + professorEmail + "\","
                        + "\"studentName\":\"" + studentName + "\","
                        + "\"date\":\"" + date + "\","
                        + "\"time\":\"" + selectedTime + "\""
                        + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                runOnUiThread(() -> {
                    if (code == 200) {
                        Toast.makeText(this, "상담 신청이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "서버 오류: " + code, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "신청 실패: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}