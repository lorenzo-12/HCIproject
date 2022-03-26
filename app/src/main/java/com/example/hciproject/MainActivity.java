package com.example.hciproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button workoutbtn,diarybtn,dietbtn,timerbtn;
    ImageButton userbtn;
    ConstraintLayout layout;
    Button debug;
    DB db = new DB();
    User tmp = new User("admin","admin");
    ArrayList<User> lista = new ArrayList<User>();


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
        debug = findViewById(R.id.buttondebug);

        lista.add(tmp);
        db.addUser(tmp);


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

        debug.setText("***"+db.users_list.size()+"***");

    }

    public void openactivityDiary(){
        Intent intentDiary = new Intent(this, MainActivity2.class);
        startActivity(intentDiary);
    }

    public void openactivityDiet(){
        Intent intentDiet = new Intent(this, MainActivity3.class);
        startActivity(intentDiet);
    }

    public void openactivityworkout(){
        Intent intentWorkout = new Intent(this, MainActivity4.class);
        startActivity(intentWorkout);
    }

    public void openactivitytimer(){
        Intent intentTimer = new Intent(this, MainActivity5.class);
        startActivity(intentTimer);
    }

    public void openactivityuser() {
        Intent intentUser = new Intent(this, MainActivity6.class);
        intentUser.putExtra("DB",db);
        //startActivity(intentUser);
        startActivityForResult(intentUser,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                db = data.getParcelableExtra("DB");
                debug.setText("***"+db.users_list.size()+"***");
            }
        }
    }
}