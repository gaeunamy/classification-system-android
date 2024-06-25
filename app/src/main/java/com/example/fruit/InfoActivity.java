package com.example.fruit;

// InfoActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // ImageButton 초기화
        ImageButton backButton = findViewById(R.id.backButton);

        // fade in 및 fade out 애니메이션 로드
        final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        // ImageButton 클릭 이벤트 처리
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fade out 애니메이션 적용
                v.startAnimation(fadeOutAnimation);

                // MainActivity로 이동
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);

                // fade in 애니메이션 적용
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                // InfoActivity 종료
                finish();
            }
        });
    }
}
