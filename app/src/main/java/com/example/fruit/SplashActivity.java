package com.example.fruit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Splash 화면에 표시할 시간 (밀리초)
    private static final int SPLASH_DISPLAY_TIME = 2000; // 2초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 일정 시간이 지난 후에 메인 액티비티로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Splash 화면을 빠져나간 후에 실행할 액티비티 지정
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
