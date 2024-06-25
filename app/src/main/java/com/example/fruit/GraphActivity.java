package com.example.fruit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    private BarChart barChart;
    private Button dateSelectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        barChart = findViewById(R.id.barChart);
        barChart.setExtraOffsets(5, 10, 10, 10);

        // ImageButton 초기화
        ImageButton backButton = findViewById(R.id.backButton);

        // ImageButton 클릭 이벤트 처리
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        barChart = findViewById(R.id.barChart);
        dateSelectionButton = findViewById(R.id.dateSelectionButton);

        // 날짜별 보기 버튼 클릭 이벤트 처리
        dateSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DateSelectionActivity로 이동
                startActivity(new Intent(GraphActivity.this, DateSelectionActivity.class));
            }
        });

        // 파이어베이스에서 normal_apple 폴더의 이미지 개수 가져오기
        FirebaseStorage.getInstance().getReference().child("rasp_detect_image/normal_apple")
                .listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult normalResult) {
                        final int normalAppleCount = normalResult.getItems().size();

                        // 파이어베이스에서 rotten_apple 폴더의 이미지 개수 가져오기
                        FirebaseStorage.getInstance().getReference().child("rasp_detect_image/rotten_apple")
                                .listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                    @Override
                                    public void onSuccess(ListResult rottenResult) {
                                        int rottenAppleCount = rottenResult.getItems().size();

                                        // 항목 개수를 그래프에 추가하기
                                        ArrayList<BarEntry> entries = new ArrayList<>();
                                        entries.add(new BarEntry(1f, normalAppleCount)); // 왼쪽에 정상 바 표시
                                        entries.add(new BarEntry(2f, rottenAppleCount)); // 불량 바 표시
                                        entries.add(new BarEntry(0f, normalAppleCount + rottenAppleCount)); // 왼쪽에 전체 바 표시

                                        BarDataSet dataSet = new BarDataSet(entries, "사과 개수");

                                        // 바 색상 설정 (사용자 정의)
                                        dataSet.setColors(new int[]{Color.rgb(76, 175, 80), Color.rgb(244, 67, 54), Color.rgb(255, 235, 59),});
                                        BarData barData = new BarData(dataSet);
                                        barChart.setData(barData);

                                        // 그래프 설정
                                        dataSet.setValueTextSize(14f);
                                        dataSet.setValueFormatter(new ValueFormatter() {
                                            @Override
                                            public String getBarLabel(BarEntry barEntry) {
                                                return String.valueOf((int) barEntry.getY());
                                            }
                                        });
                                        barChart.getDescription().setEnabled(false);
                                        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"전체", "정상", "불량"})); // X축 레이블 추가
                                        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                        barChart.getXAxis().setGranularity(1f);
                                        barChart.getXAxis().setTextSize(14f);
                                        barChart.getAxisLeft().setTextSize(14f);

                                        // 오른쪽 y축 표시 제거
                                        barChart.getAxisRight().setEnabled(false);

                                        // y축 스케일 조정
                                        barChart.getAxisLeft().setAxisMinimum(0f); // y축 최소값 설정
                                        barChart.getAxisLeft().setAxisMaximum(normalAppleCount + rottenAppleCount); // y축 최대값 설정

                                        barChart.getLegend().setEnabled(false);

                                        barChart.invalidate(); // 그래프 새로고침
                                    }
                                });
                    }
                });
    }
}
