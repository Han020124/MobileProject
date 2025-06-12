package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class graduation_main extends AppCompatActivity {

    BarChart barChart;
    Button btnInput, btnEdit;
    ImageButton btnArrow; // 추가: 상단 화살표 버튼
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graduation_main); // 레이아웃 파일명 확인

        barChart = findViewById(R.id.barChart);
        btnInput = findViewById(R.id.btnInput);
        btnEdit = findViewById(R.id.btnEdit);
        btnArrow = findViewById(R.id.btn_arrow); // 버튼 바인딩
        dbHelper = new DBHelper(this);

        showChart();

        // 과목 입력 화면으로 이동
        btnInput.setOnClickListener(v -> {
            Intent intent = new Intent(graduation_main.this, graduation_regi.class);
            startActivity(intent);
        });

        // 과목 편집 화면으로 이동
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(graduation_main.this, graduation_edit.class);
            startActivity(intent);
        });

        // 화살표 버튼 클릭 시 홈으로 이동
        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(graduation_main.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showChart() {
        HashMap<String, Integer> categoryMap = dbHelper.getCategoryCreditSum(); // 이수 구분별 학점 데이터

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for (String category : categoryMap.keySet()) {
            entries.add(new BarEntry(index, categoryMap.get(category)));
            labels.add(category);
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "이수구분별 학점");
        dataSet.setColors(new int[]{R.color.purple_200, R.color.purple_500, R.color.teal_200, R.color.teal_700}, this);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        barChart.invalidate(); // 차트 갱신
    }
}
