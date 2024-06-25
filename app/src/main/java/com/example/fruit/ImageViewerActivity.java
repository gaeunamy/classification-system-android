package com.example.fruit;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 인텐트로 전달된 이미지 URL과 이름을 받아옵니다.
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String imageName = getIntent().getStringExtra("imageName");

        // 툴바의 제목으로 이미지 이름을 설정합니다.
        getSupportActionBar().setTitle(imageName);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        ImageView imageView = findViewById(R.id.imageView);

        // Glide나 Picasso 등의 라이브러리를 사용하여 이미지를 표시합니다.
        Glide.with(this).load(imageUrl).into(imageView);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
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
