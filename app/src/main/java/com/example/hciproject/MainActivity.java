package com.example.hciproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    Button workoutbtn,diarybtn,dietbtn,timerbtn;
    ImageButton userbtn;
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.constraintLayout);
        layout.setBackgroundColor(Color.WHITE);
        workoutbtn = findViewById(R.id.WorkoutButton);
        diarybtn = findViewById(R.id.DiaryButton);
        dietbtn = findViewById(R.id.DietButton);
        timerbtn = findViewById(R.id.TimerButton);
        userbtn = findViewById(R.id.userbutton);


        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openactivityuser();}
        });

        diarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiary();
            }
        });

        dietbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiet();

            }
        });

        workoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityworkout();

            }
        });

        timerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivitytimer();

            }
        });
    }

    public void openactivityDiary(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void openactivityDiet(){
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    public void openactivityworkout(){
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }

    public void openactivitytimer(){
        Intent intent = new Intent(this, MainActivity5.class);
        startActivity(intent);
    }

    public void openactivityuser(){
        Intent intent = new Intent(this, MainActivity6.class);
        startActivity(intent);
    }
}