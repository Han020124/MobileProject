package com.example.mobileproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class graduation_edit extends AppCompatActivity {

    ListView listView;
    Button btnDelete;
    ImageButton btnArrow;
    DBHelper dbHelper;
    ArrayList<String> subjectList;
    ArrayAdapter<String> adapter;

    String selectedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graduation_edit);

        listView = findViewById(R.id.list_subjects);
        btnDelete = findViewById(R.id.btnDelete);
        btnArrow = findViewById(R.id.btn_arrow);

        dbHelper = new DBHelper(this);
        subjectList = new ArrayList<>();

        loadSubjectData();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedItem = subjectList.get(i);
            Toast.makeText(this, "선택: " + selectedItem, Toast.LENGTH_SHORT).show();
        });

        btnDelete.setOnClickListener(view -> {
            if (selectedItem != null) {
                new AlertDialog.Builder(this)
                        .setTitle("삭제 확인")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("삭제", (dialogInterface, i) -> {
                            String[] parts = selectedItem.split("\\|");
                            String subjectName = parts[0].trim();
                            dbHelper.deleteSubject(subjectName);
                            loadSubjectData();
                            selectedItem = null;
                        })
                        .setNegativeButton("취소", null)
                        .show();
            } else {
                Toast.makeText(this, "삭제할 과목을 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });

        btnArrow.setOnClickListener(v -> finish());
    }

    private void loadSubjectData() {
        subjectList.clear();
        Cursor cursor = dbHelper.getAllSubjects();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int credit = cursor.getInt(1);
            String category = cursor.getString(2);
            subjectList.add(name + " | " + credit + "학점 | " + category);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectList);
        listView.setAdapter(adapter);
    }
}
