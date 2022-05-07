package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    public String user_img_path;
    Boolean insertFood = false;


    //variabili globali usate dalla Main page
    Button workoutActivitybtn, diaryActivitybtn, FoodActivitybtn,timerbtn;
    ImageView userView;
    Button debug;
    DBHelper db;
    BottomNavigationView nav;

    @Override
    public void onResume() {
        // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        loadData();
        if (user_logged.equals("none")) userView.setImageResource(R.drawable.no_image2);
        else {
            debug.setText("USER: "+user_logged);
            userView.setImageBitmap(db.loadImage(user_logged));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //associo a ogni variabile il corrispettivo bottone/testo ecc...
        //layout = findViewById(R.id.constraintLayout);
        //layout.setBackgroundColor(Color.WHITE);
        workoutActivitybtn = findViewById(R.id.ExerciseButton);
        diaryActivitybtn = findViewById(R.id.DiaryButton);
        FoodActivitybtn = findViewById(R.id.FoodButton);
        timerbtn = findViewById(R.id.TimerButton);
        userView = findViewById(R.id.userbutton);
        debug = findViewById(R.id.buttondebug);

        workoutActivitybtn.setVisibility(View.INVISIBLE);
        diaryActivitybtn.setVisibility(View.INVISIBLE);
        FoodActivitybtn.setVisibility(View.INVISIBLE);
        timerbtn.setVisibility(View.INVISIBLE);
        //userView.setVisibility(View.INVISIBLE);

        workoutActivitybtn.setClickable(false);
        diaryActivitybtn.setClickable(false);
        FoodActivitybtn.setClickable(false);
        timerbtn.setClickable(false);
        userView.setClickable(false);

        nav = findViewById(R.id.bottomnavigatorview);
        //così quando apro l'app mi da fin  da subito selezionata l'icona del diario
        nav.setSelectedItemId(R.id.bottom_diary);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_diary:
                        Toast.makeText(MainActivity.this,"diary",Toast.LENGTH_SHORT).show();
                        //do nothing since we are already in the Diary activity
                        return true;
                    case R.id.bottom_exercise:
                        Toast.makeText(MainActivity.this,"exercise",Toast.LENGTH_SHORT).show();
                        openactivityexercise();
                        return true;
                    case R.id.bottom_food:
                        Toast.makeText(MainActivity.this,"food",Toast.LENGTH_SHORT).show();
                        openactivityFood();
                        return true;
                    case R.id.bottom_time:
                        Toast.makeText(MainActivity.this,"timer",Toast.LENGTH_SHORT).show();
                        openactivitytimer();
                        return true;
                    case R.id.bottom_user:
                        Toast.makeText(MainActivity.this,"user",Toast.LENGTH_SHORT).show();
                        openactivityuser();
                        return true;
                }
                return true;
            }
        });


        loadData();
        debug.setText("User: "+user_logged);

        db = new DBHelper(this);
        if (!user_logged.equals("none")) {
            userView.setImageBitmap(db.loadImage(user_logged));
        }
        else userView.setImageResource(R.drawable.no_image2);

        if (insertFood == false) {
            db.addExistingFood();
            db.addExistingExercise();
            insertFood = true;
            saveData();
        }
    }

    //relative funzioni che vengono chiamete quando premiamo un bottone
    public void openactivityDiary(){
        saveData();
        Intent intentDiary = new Intent(this, MainActivity.class);
        startActivity(intentDiary);
    }

    public void openactivityFood(){
        saveData();
        Toast.makeText(MainActivity.this,"Loading",Toast.LENGTH_LONG).show();
        Intent intentDiet = new Intent(this, FoodPage.class);
        startActivity(intentDiet);
    }

    public void openactivityexercise(){
        saveData();
        Intent intentExercise = new Intent(this, ExercisePage.class);
        startActivity(intentExercise);
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