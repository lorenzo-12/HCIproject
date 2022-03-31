package com.example.hciproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;

public class DiaryPage extends AppCompatActivity {

    ConstraintLayout diarylayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarypage);

        diarylayout = findViewById(R.id.constraintLayout);
        diarylayout.setBackgroundColor(Color.GREEN);
    }


}