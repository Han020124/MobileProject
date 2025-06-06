package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageButton buttonAttendance;
    private ImageButton buttonSchedule;
    private ImageButton buttonEvaluation;
    private ImageButton buttonBus;
    private ImageButton buttonGraduation;
    private ImageButton buttonCounseling;
    private ImageButton buttonAbsence;
    private ImageButton buttonPartnership;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setClickListeners();
    }

    private void initViews() {
        buttonAttendance = findViewById(R.id.button_attendance);
        buttonSchedule = findViewById(R.id.button_schedule);
        buttonEvaluation = findViewById(R.id.button_evaluation);
        buttonBus = findViewById(R.id.button_bus);
        buttonGraduation = findViewById(R.id.button_graduation);
        buttonCounseling = findViewById(R.id.button_counseling);
        buttonAbsence = findViewById(R.id.button_absence);
        buttonPartnership = findViewById(R.id.button_partnership);
    }

    private void setClickListeners() {
        buttonAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AttendanceActivity.class);
            startActivity(intent);
        });

        buttonSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TimetableActivity.class);
            startActivity(intent);
        });

        buttonEvaluation.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EvaluationActivity.class);
            startActivity(intent);
        });

        buttonBus.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BusScheduleActivity.class);
            startActivity(intent);
        });

        buttonGraduation.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GraduationActivity.class);
            startActivity(intent);
        });

        buttonCounseling.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ConsultationActivity.class);
            startActivity(intent);
        });

        buttonAbsence.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RequestAbsenceActivity.class);
            startActivity(intent);
        });

        buttonPartnership.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PartnercompanyActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finishAffinity();
    }
}