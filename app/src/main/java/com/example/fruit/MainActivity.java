package com.example.fruit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 툴바 제목 변경
        getSupportActionBar().setTitle("데이터 판별");

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        Button allButton = findViewById(R.id.btn_all);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AllData.java로 이동
                Intent allIntent = new Intent(MainActivity.this, AllData.class);
                startActivity(allIntent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        Button normalButton = findViewById(R.id.btn_normal);
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NormalData.java로 이동
                Intent normalIntent = new Intent(MainActivity.this, NormalData.class);
                startActivity(normalIntent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        Button rottenButton = findViewById(R.id.btn_rotten);
        rottenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RottenData.java로 이동
                Intent rottenIntent = new Intent(MainActivity.this, RottenData.class);
                startActivity(rottenIntent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }
}



