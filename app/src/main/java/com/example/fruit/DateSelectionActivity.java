package com.example.fruit;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateSelectionActivity extends AppCompatActivity {

    private Button btnStartDate;
    private Button showDataButton;
    private BarChart barChart;

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        btnStartDate = findViewById(R.id.btnStartDate);
        showDataButton = findViewById(R.id.showDataButton);
        barChart = findViewById(R.id.barChart);

        barChart.setExtraOffsets(5, 10, 10, 10);
        barChart.getDescription().setEnabled(false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // 현재 날짜를 가져와서 버튼에 표시
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        btnStartDate.setText(year + "년 " + (month + 1) + "월 " + day + "일");

        // 오늘 데이터 표시
        getDataForSelectedDate(day, month, year);

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 날짜로 DatePickerDialog 열기
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DateSelectionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // 날짜를 선택하면 EditText에 표시
                                btnStartDate.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");

                                // 선택한 날짜로 데이터 가져오기
                                getDataForSelectedDate(dayOfMonth, month, year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        showDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 선택한 날짜 가져오기
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // 선택한 날짜에 해당하는 데이터 가져오기
                getDataForSelectedDate(day, month, year);
            }
        });
    }

    private void getDataForSelectedDate(int day, int month, int year) {
        // 선택한 날짜로 Calendar 객체 생성
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date selectedDate = calendar.getTime();

        // 선택한 날짜의 끝 시간 설정 (23시 59분 59초)
        calendar.set(year, month, day, 23, 59, 59);
        Date endDate = calendar.getTime();

        db.collection("fruits")
                .whereGreaterThanOrEqualTo("lastModified", new Timestamp(selectedDate))
                .whereLessThan("lastModified", new Timestamp(endDate))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // 성공적으로 데이터를 가져왔을 때 처리
                        int 정상_데이터 = 0; // 정상 데이터 개수
                        int 불량_데이터 = 0; // 불량 데이터 개수

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // 여기서 데이터를 가져와서 필요한 처리를 수행
                            String 상태 = document.getString("status");
                            if (상태 != null) {
                                if (상태.equals("정상")) {
                                    정상_데이터++;
                                } else if (상태.equals("불량")) {
                                    불량_데이터++;
                                }
                            }
                        }

                        // Firebase Storage에서 해당 날짜의 파일 개수 가져오기
                        getRaspDetectImageCount(year, month, day, 정상_데이터, 불량_데이터);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 데이터 가져오기 실패 시 처리
                        Toast.makeText(DateSelectionActivity.this, "데이터 가져오기 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getRaspDetectImageCount(int year, int month, int day, int 정상_데이터, int 불량_데이터) {
        String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
        StorageReference normalRef = storage.getReference().child("rasp_detect_image/normal_apple");
        StorageReference rottenRef = storage.getReference().child("rasp_detect_image/rotten_apple");

        // 정상 파일 개수 가져오기
        normalRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult normalListResult) {
                        // 불량 파일 개수 가져오기
                        rottenRef.listAll()
                                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                    @Override
                                    public void onSuccess(ListResult rottenListResult) {
                                        int normalCount = 0;
                                        int rottenCount = 0;

                                        // 정상 파일 중 선택한 날짜에 해당하는 파일 개수 확인
                                        for (StorageReference normalRef : normalListResult.getItems()) {
                                            String fileName = normalRef.getName();
                                            if (fileName.contains(selectedDate)) {
                                                normalCount++;
                                            }
                                        }

                                        // 불량 파일 중 선택한 날짜에 해당하는 파일 개수 확인
                                        for (StorageReference rottenRef : rottenListResult.getItems()) {
                                            String fileName = rottenRef.getName();
                                            if (fileName.contains(selectedDate)) {
                                                rottenCount++;
                                            }
                                        }

                                        // 가져온 데이터를 그래프에 반영
                                        int 전체_데이터 = normalCount + rottenCount;
                                        updateChart(normalCount, rottenCount, 전체_데이터);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 불량 파일 개수 가져오기 실패 시 처리
                                        Toast.makeText(DateSelectionActivity.this, "불량 파일 개수 가져오기 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 정상 파일 개수 가져오기 실패 시 처리
                        Toast.makeText(DateSelectionActivity.this, "정상 파일 개수 가져오기 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateChart(int normalCount, int rottenCount, int 전체_데이터) {
        // 그래프 데이터 설정
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 전체_데이터)); // 전체
        entries.add(new BarEntry(1, normalCount)); // 정상
        entries.add(new BarEntry(2, rottenCount)); // 불량

        BarDataSet barDataSet = new BarDataSet(entries, "통계");
        barDataSet.setColors(new int[]{Color.rgb(255, 235, 59), Color.rgb(76, 175, 80), Color.rgb(244, 67, 54)});

        // Set the custom formatter for the bar labels
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.valueOf((int) barEntry.getY());
            }
        });

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barDataSet.setValueTextSize(14f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 위치를 아래로 설정
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"전체", "정상", "불량"})); // X축 레이블 설정
        xAxis.setGranularity(1f); // X축 간격을 1로 설정
        xAxis.setTextSize(14f);
        xAxis.setLabelCount(3); // X축 라벨의 개수를 3으로 설정하여 세로축이 3개만 나오도록 설정

        // Set custom formatter for Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%d", (int) value);
            }
        });
        leftAxis.setAxisMinimum(0f); // Y축 최소값 설정
        leftAxis.setGranularity(1f); // Y축 간격을 1로 설정
        leftAxis.setAxisMaximum(전체_데이터); // Y축 최대값 설정
        leftAxis.setTextSize(14f);

        barChart.getAxisRight().setEnabled(false); // 오른쪽 Y축 비활성화

        barChart.invalidate(); // 차트를 갱신하여 변경 사항을 반영

        barChart.getLegend().setEnabled(false);
    }
}
