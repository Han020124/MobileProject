package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etStudentId;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private EditText etDepartment;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etStudentId = findViewById(R.id.et_student_id);
        etPassword = findViewById(R.id.et_password);
        etPasswordCheck = findViewById(R.id.et_password_check);
        etDepartment = findViewById(R.id.et_department);
        buttonSignup = findViewById(R.id.button_signup);
    }

    private void setupClickListeners() {
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });
    }

    private void performSignup() {
        String studentId = etStudentId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordCheck = etPasswordCheck.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();

        if (!validateInputs(studentId, password, passwordCheck, department)) {
            return;
        }

        if (registerUser(studentId, password, department)) {
            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

            navigateToLogin();
        } else {
            Toast.makeText(this, "회원가입에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String studentId, String password, String passwordCheck, String department) {

        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("학번을 입력해주세요.");
            etStudentId.requestFocus();
            return false;
        }

        if (studentId.length() < 8) {
            etStudentId.setError("8자리 이상이어야 합니다.");
            etStudentId.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("비밀번호를 입력해주세요.");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("비밀번호는 6자리 이상이어야 합니다.");
            etPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(passwordCheck)) {
            etPasswordCheck.setError("비밀번호 확인을 입력해주세요.");
            etPasswordCheck.requestFocus();
            return false;
        }

        if (!password.equals(passwordCheck)) {
            etPasswordCheck.setError("비밀번호가 일치하지 않습니다.");
            etPasswordCheck.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(department)) {
            etDepartment.setError("학과를 입력해주세요.");
            etDepartment.requestFocus();
            return false;
        }

        return true;
    }

    private boolean registerUser(String studentId, String password, String department) {
        // 서버 API 호출, 로컬 데이터베이스에 저장


        try {
            // 회원가입 로직 구현

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToLogin();
    }
}