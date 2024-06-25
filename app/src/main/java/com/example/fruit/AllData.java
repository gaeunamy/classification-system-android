package com.example.fruit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;

public class AllData extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        FirebaseApp.initializeApp(this);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
        refreshButton = findViewById(R.id.refreshButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("전체 사과 리스트");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

        loadImagesFromFirebase();
    }

    private void loadImagesFromFirebase() {
        ArrayList<DataItem> dataList = new ArrayList<>();
        fetchImagesFromFolder("rasp_detect_image/normal_apple", dataList, new OnCompleteListener() {
            @Override
            public void onComplete() {
                fetchImagesFromFolder("rasp_detect_image/rotten_apple", dataList, new OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        // 리스트를 날짜와 시간 순으로 정렬
                        Collections.sort(dataList);

                        // 정렬된 순서대로 번호 부여
                        int number = 1;
                        for (DataItem item : dataList) {
                            item.setNumber(number++);
                        }

                        DataAdapter adapter = new DataAdapter(AllData.this, dataList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllData.this));

                        adapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(DataItem dataItem) {
                                Intent intent = new Intent(AllData.this, ImageViewerActivity.class);
                                intent.putExtra("imageName", dataItem.getFileName());
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

    private void fetchImagesFromFolder(String folderPath, ArrayList<DataItem> dataList, OnCompleteListener listener) {
        FirebaseStorage.getInstance().getReference().child(folderPath)
                .listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference storageReference : listResult.getItems()) {
                            String fileName = storageReference.getName();
                            String dateTime = extractDateTimeFromFileName(fileName);
                            dataList.add(new DataItem(0, fileName, dateTime)); // 일단 0으로 초기화
                        }
                        listener.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AllData.this, "이미지를 로드하는 데 오류가 발생했습니다: " + folderPath, Toast.LENGTH_SHORT).show();
                        listener.onComplete();
                    }
                });
    }

    private String extractDateTimeFromFileName(String fileName) {
        // 파일 이름에서 확장자 ".jpg"를 제거합니다.
        String nameWithoutExtension = fileName.replace(".jpg", "");

        // 파일 이름에서 날짜와 시간을 추출하는 로직을 작성합니다.
        // 파일 이름이 normal_apple_2023-12-25_13:46:48 형식이라면 "_"를 기준으로 나눈 후 세 번째 부분부터 추출합니다.
        String[] parts = nameWithoutExtension.split("_");
        StringBuilder dateTimeBuilder = new StringBuilder();
        if (parts.length >= 3) {
            // Date와 Time을 따로 표시하기 위해 인덱스 2에서 Date를 추출하고, 인덱스 3부터는 Time을 추출합니다.
            dateTimeBuilder.append(parts[2]); // Date
            dateTimeBuilder.append("\n");
            for (int i = 3; i < parts.length; i++) {
                dateTimeBuilder.append(parts[i]);
                if (i < parts.length - 1) {
                    dateTimeBuilder.append("_"); // 시간 부분 간에 "_"로 연결
                }
            }
            return dateTimeBuilder.toString();
        } else {
            return "날짜 없음";
        }
    }

    private void refreshData() {
        loadImagesFromFirebase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private interface OnCompleteListener {
        void onComplete();
    }
}
