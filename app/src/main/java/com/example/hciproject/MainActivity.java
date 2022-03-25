package com.example.hciproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button workoutbutton,diarybutton,dietbutton,timerbutton;
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.constraintLayout);
        layout.setBackgroundColor(Color.WHITE);
        workoutbutton = findViewById(R.id.WorkoutButton);
        diarybutton = findViewById(R.id.DiaryButton);
        dietbutton = findViewById(R.id.DietButton);
        timerbutton = findViewById(R.id.TimerButton);

        //diarybutton.setOnClickListener(view -> layout.setBackgroundColor(Color.GREEN));
        //dietbutton.setOnClickListener(view -> layout.setBackgroundColor(Color.RED));
        //workoutbutton.setOnClickListener(view -> layout.setBackgroundColor(Color.BLUE));
        //timerbutton.setOnClickListener(view -> layout.setBackgroundColor(Color.YELLOW));

        diarybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiary();
            }
        });

        dietbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiet();

            }
        });

        workoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityworkout();

            }
        });

        timerbutton.setOnClickListener(new View.OnClickListener() {
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
}