package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    Boolean insertFood = false;


    //variabili globali usate dalla Main page
    Button workoutActivitybtn, diaryActivitybtn, FoodActivitybtn,timerbtn;
    ImageButton userbtn;
    ConstraintLayout layout;
    Button debug;
    DBHelper db;

    @Override
    public void onResume() {
        // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        loadData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //associo a ogni variabile il corrispettivo bottone/testo ecc...
        layout = findViewById(R.id.constraintLayout);
        //layout.setBackgroundColor(Color.WHITE);
        workoutActivitybtn = findViewById(R.id.ExerciseButton);
        diaryActivitybtn = findViewById(R.id.DiaryButton);
        FoodActivitybtn = findViewById(R.id.FoodButton);
        timerbtn = findViewById(R.id.TimerButton);
        userbtn = findViewById(R.id.userbutton);
        debug = findViewById(R.id.buttondebug);


        //setto per ogni bottone la rispttiva funzione ONCLICK

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openactivityuser();}
        });

        diaryActivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiary();
            }
        });

        FoodActivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityFood();
            }
        });

        workoutActivitybtn.setOnClickListener(new View.OnClickListener() {
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

        loadData();
        debug.setText("User: "+user_logged);

        db = new DBHelper(this);
        if (!user_logged.equals("none")){
            Cursor cursor = db.readAllDataUser();
            if (cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if (cursor.getString(0).equals(user_logged)){
                        byte[] img_bytes = cursor.getBlob(2);
                        Bitmap img_b = db.getImage(img_bytes);
                        userbtn.setImageBitmap(img_b);
                    }
                }
            }
        }
        loadData();
        if (insertFood == false) {
            db.addExistingFood();
            insertFood = true;
            saveData();
        }
    }

    //relative funzioni che vengono chiamete quando premiamo un bottone
    public void openactivityDiary(){
        saveData();
        Intent intentDiary = new Intent(this, DiaryPage.class);
        startActivity(intentDiary);
    }

    public void openactivityFood(){
        saveData();
        Toast.makeText(MainActivity.this,"Loading",Toast.LENGTH_LONG).show();
        Intent intentDiet = new Intent(this, FoodPage.class);
        startActivity(intentDiet);
    }

    public void openactivityworkout(){
        saveData();
        Intent intentWorkout = new Intent(this, ExercisePage.class);
        startActivity(intentWorkout);
    }

    public void openactivitytimer(){
        saveData();
        Intent intentTimer = new Intent(this, TimerPage.class);
        startActivity(intentTimer);
    }

    public void openactivityuser() {
        saveData();
        Intent intentUser;
        //creo la nuova pagina (intentUser)
        intentUser = new Intent(this, UserPage.class);
        //se non mi interessa ricevere delle informazioni dalla pagina figlia allora posso usare
        //direttamente STARTACTIVITY
        startActivity(intentUser);

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGGED,user_logged);
        editor.putBoolean("base_food_insert",insertFood);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
        insertFood = sharedPreferences.getBoolean("base_food_insert",false);
    }
}