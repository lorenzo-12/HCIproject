package com.example.hciproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    ConstraintLayout diarylayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        diarylayout = findViewById(R.id.constraintLayout);
        diarylayout.setBackgroundColor(Color.GREEN);
    }


}