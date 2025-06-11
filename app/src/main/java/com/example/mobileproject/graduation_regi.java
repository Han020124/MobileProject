package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class graduation_regi extends AppCompatActivity {

    Spinner spinnerSubject, spinnerCredit, spinnerClass;
    Button buttonConfirm;
    DBHelper dbHelper;  // SQLite DB 헬퍼 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graduation_regi); // XML 파일 이름에 맞게 수정

        spinnerSubject = findViewById(R.id.spinner_sub);
        spinnerCredit = findViewById(R.id.spinner_crd);
        spinnerClass = findViewById(R.id.spinner_cls);
        buttonConfirm = findViewById(R.id.button_confirm);

        dbHelper = new DBHelper(this);

        // Spinner 데이터 설정
        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(
                this, R.array.subject_list, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        ArrayAdapter<CharSequence> creditAdapter = ArrayAdapter.createFromResource(
                this, R.array.credit_list, android.R.layout.simple_spinner_item);
        creditAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCredit.setAdapter(creditAdapter);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(
                this, R.array.class_type_list, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        // 확인 버튼 클릭 이벤트
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = spinnerSubject.getSelectedItem().toString();
                int credit = Integer.parseInt(spinnerCredit.getSelectedItem().toString());
                String classType = spinnerClass.getSelectedItem().toString();

                boolean inserted = dbHelper.insertSubject(subject, credit, classType);
                if (inserted) {
                    Toast.makeText(graduation_regi.this, "과목이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(graduation_regi.this, "저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
