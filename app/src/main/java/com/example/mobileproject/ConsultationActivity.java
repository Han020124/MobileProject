package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ConsultationActivity extends AppCompatActivity {

    private ImageButton btnArrow;
    private Spinner spinnerProfessor;
    private Spinner spinnerDate;
    private Button buttonApply;

    private String selectedTime = "";
    private List<Button> timeButtons = new ArrayList<>();
    private Button selectedTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        initializeViews();
        setupSpinners();
        setupTimeButtons();
        setupClickListeners();
    }

    private void initializeViews() {
        btnArrow = findViewById(R.id.btn_arrow);
        spinnerProfessor = findViewById(R.id.spinner_professor);
        spinnerDate = findViewById(R.id.spinner_date);
        buttonApply = findViewById(R.id.button_apply);

        timeButtons.add(findViewById(R.id.btn_0900));
        timeButtons.add(findViewById(R.id.btn_0930));
        timeButtons.add(findViewById(R.id.btn_1000));
        timeButtons.add(findViewById(R.id.btn_1030));
        timeButtons.add(findViewById(R.id.btn_1100));
        timeButtons.add(findViewById(R.id.btn_1130));
        timeButtons.add(findViewById(R.id.btn_1300));
        timeButtons.add(findViewById(R.id.btn_1330));
        timeButtons.add(findViewById(R.id.btn_1400));
        timeButtons.add(findViewById(R.id.btn_1430));
        timeButtons.add(findViewById(R.id.btn_1500));
        timeButtons.add(findViewById(R.id.btn_1530));
        timeButtons.add(findViewById(R.id.btn_1530_2)); // 중복된 ID 수정 필요
        timeButtons.add(findViewById(R.id.btn_1600));
        timeButtons.add(findViewById(R.id.btn_1630));
        timeButtons.add(findViewById(R.id.btn_1700));
        timeButtons.add(findViewById(R.id.btn_1730));
        timeButtons.add(findViewById(R.id.btn_1800));
    }

    private void setupSpinners() {
        List<String> professors = new ArrayList<>();
        professors.add("교수님을 선택하세요");
        professors.add("김ㅇㅇ");
        professors.add("이ㅇㅇ");
        professors.add("박ㅇㅇ");
        professors.add("최ㅇㅇ");
        professors.add("황ㅇㅇ");

        ArrayAdapter<String> professorAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                professors
        );
        professorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfessor.setAdapter(professorAdapter);

        List<String> dates = new ArrayList<>();
        dates.add("날짜를 선택하세요");
        dates.add("2025-06-08 (일요일)");
        dates.add("2025-06-09 (월요일)");
        dates.add("2025-06-10 (화요일)");
        dates.add("2025-06-11 (수요일)");
        dates.add("2025-06-12 (목요일)");
        dates.add("2025-06-13 (금요일)");
        dates.add("2025-06-14 (토요일)");

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dates
        );
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(dateAdapter);
    }

    private void setupTimeButtons() {
        for (Button button : timeButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTimeButton((Button) v);
                }
            });
        }
    }

    private void selectTimeButton(Button clickedButton) {
        if (selectedTimeButton != null) {
            selectedTimeButton.setBackgroundResource(R.drawable.button_time_background);
            selectedTimeButton.setTextColor(getResources().getColor(android.R.color.black));
        }

        selectedTimeButton = clickedButton;
        selectedTime = clickedButton.getText().toString();

        clickedButton.setBackgroundResource(R.drawable.button_background);
        clickedButton.setTextColor(getResources().getColor(android.R.color.white));

        Toast.makeText(this, selectedTime + " 시간이 선택되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void setupClickListeners() {
        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyConsultation();
            }
        });
    }

    private void applyConsultation() {
        String selectedProfessor = spinnerProfessor.getSelectedItem().toString();
        String selectedDate = spinnerDate.getSelectedItem().toString();

        if (selectedProfessor.equals("교수님을 선택하세요")) {
            Toast.makeText(this, "교수님을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.equals("날짜를 선택하세요")) {
            Toast.makeText(this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTime.isEmpty()) {
            Toast.makeText(this, "시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = String.format(
                "면담 신청이 완료되었습니다.\n교수님: %s\n날짜: %s\n시간: %s",
                selectedProfessor,
                selectedDate,
                selectedTime
        );

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // 데이터 전송, 데이터베이스에 저장

        Intent intent = new Intent(ConsultationActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConsultationActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}