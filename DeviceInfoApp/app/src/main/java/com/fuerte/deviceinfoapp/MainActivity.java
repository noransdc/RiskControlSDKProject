package com.fuerte.deviceinfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_app).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this, "app");
        });

        findViewById(R.id.btn_contact).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this, "contact");

        });

        findViewById(R.id.btn_device).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this, "device");

        });

        findViewById(R.id.btn_location).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this, "location");

        });

        findViewById(R.id.btn_album).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this, "album");

        });

        findViewById(R.id.btn_sms).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this, "sms");

        });

        findViewById(R.id.btn_calender).setOnClickListener(v -> {
            ShowActivity.start(MainActivity.this ,"calender");
        });


    }





}