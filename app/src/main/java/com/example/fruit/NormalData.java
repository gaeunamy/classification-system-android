package com.example.fruit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Comparator;

public class NormalData extends AppCompatActivity {

    private ImageView refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        FirebaseApp.initializeApp(this);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        refreshButton = findViewById(R.id.refreshButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("정상 사과 리스트");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(); // 새로고침 버튼 클릭 시 데이터를 새로고침하는 메서드 호출
            }
        });

        loadImagesFromFirebase(recyclerView); // 데이터 로드
    }

    private void loadImagesFromFirebase(RecyclerView recyclerView) {
        FirebaseStorage.getInstance().getReference().child("rasp_detect_image/normal_apple")
                .listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        ArrayList<Image> arrayList = new ArrayList<>();
                        ImageAdapter adapter = new ImageAdapter(NormalData.this, arrayList);
                        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(Image image) {
                                // 이미지를 클릭하면 ImageViewerActivity를 시작하고 이미지 URL을 전달합니다.
                                Intent intent = new Intent(NormalData.this, ImageViewerActivity.class);
                                intent.putExtra("imageUrl", image.getUrl());
                                intent.putExtra("imageName", image.getName());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);

                        // 데이터를 이름 기준으로 내림차순 정렬하여 리스트에 추가
                        ArrayList<StorageReference> storageReferences = new ArrayList<>(listResult.getItems());
                        Collections.sort(storageReferences, new Comparator<StorageReference>() {
                            @Override
                            public int compare(StorageReference o1, StorageReference o2) {
                                return o2.getName().compareTo(o1.getName()); // 내림차순 정렬
                            }
                        });

                        for (StorageReference storageReference : storageReferences) {
                            Image image = new Image();
                            image.setName(storageReference.getName());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    image.setUrl(url);
                                    arrayList.add(image);
                                    adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NormalData.this, "이미지를 로드하는 데 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NormalData.this, "이미지를 로드하는 데 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refreshData() {
        // 데이터를 새로고침하는 작업을 수행할 메서드를 여기에 추가
        loadImagesFromFirebase((RecyclerView) findViewById(R.id.recycler)); // 예시로 다시 데이터 로드
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
}
