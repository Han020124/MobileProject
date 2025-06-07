package com.example.mobileproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Random;

public class GraduationMainActivity extends AppCompatActivity {
    private final String[] APPS = {"전공(필수)", "전공(선택)", "교양(필수)", "교양(선택)", "합계"};
    private final int MAX_X_VALUE = APPS.length;
    private final int MAX_Y_VALUE = 6;
    private final int MIN_Y_VALUE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graduation_main);

        HorizontalBarChart major_essential = (HorizontalBarChart)findViewById(R.id.major_essential);
        configureChartAppearance(major_essential);
        prepareChartData(createChartData(), major_essential);
    }
    private void prepareChartData(BarData data, HorizontalBarChart barChart) {
        barChart.setData(data); // BarData 전달
        barChart.invalidate(); // BarChart 갱신해 데이터 표시
    }

    private void configureChartAppearance(HorizontalBarChart barChart) {

        barChart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        barChart.setTouchEnabled(false); // 터치 유무
        barChart.getLegend().setEnabled(false); // Legend는 차트의 범례
        barChart.setExtraOffsets(10f, 0f, 40f, 0f);

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setGridLineWidth(25f);
        xAxis.setGridColor(Color.parseColor("#80E5E5E5"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치

        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f); // 최솟값
        axisLeft.setAxisMaximum(50f); // 최댓값
        axisLeft.setGranularity(1f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false); // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);

        // XAxis에 원하는 String 설정하기 (앱 이름)
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return APPS[(int) value];
            }
        });
    }
    private BarData createChartData() {
        int total = 0;
        
        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            int x = i;

            if(i < MAX_X_VALUE-1){
                int y = new Random().nextInt(MAX_Y_VALUE) + MIN_Y_VALUE;
                total += y;

                values.add(new BarEntry(x, y));
            }else{

                values.add(new BarEntry(x, total));
                System.out.println(total);
            }
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet barDataSet = new BarDataSet(values, "SET_LABEL");
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(true);
        barDataSet.setColor(Color.parseColor("#66767676")); // 색상 설정
        // 데이터 값 원하는 String 포맷으로 설정하기 (ex. ~회)
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (String.valueOf((int) value)) + "점";
            }
        });

        // 3. [BarData] 보여질 데이터 구성
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f);
        data.setValueTextSize(15);

        return data;
    }
}