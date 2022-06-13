package com.example.hciproject;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    public String user_img_path;
    public String current_date;
    Boolean FirstAccess = false;



    //variabili globali usate dalla Main page
    TextView database;
    Button workoutActivitybtn, diaryActivitybtn, FoodActivitybtn,timerbtn;
    ImageView userView;
    Button debug,button;
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
        userView = findViewById(R.id.userbutton);
        debug = findViewById(R.id.buttondebug);
        database = findViewById(R.id.databse);

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
        else {
            openactivityFitlife();
            userView.setImageResource(R.drawable.no_image2);
        }

        if (FirstAccess == false) {
            startThreadFood();
            startThreadExercise();
        }

        //TODO
        Calendar c = Calendar.getInstance();
        current_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        database.setText(db.viewUsers()+current_date);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(currentDateString);
        //TODO
        database = findViewById(R.id.databse);
        current_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        database.setText(current_date);
    }


    //relative funzioni che vengono chiamete quando premiamo un bottone
    public void openactivityDiary(){
        saveData();
        Intent intentDiary = new Intent(this, MainActivity.class);
        startActivity(intentDiary);
    }

    public void openactivityFitlife(){
        saveData();
        Intent intentFitlife = new Intent(this, FitlifePage.class);
        startActivity(intentFitlife);
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
        editor.putBoolean("FirstAccess", FirstAccess);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
        FirstAccess = sharedPreferences.getBoolean("FirstAccess",false);
    }



    public void startThreadFood(){
        FoodThread t = new FoodThread();
        t.start();
    }

    public void startThreadExercise(){
        ExerciseThread t = new ExerciseThread();
        t.start();
    }

    class FoodThread extends Thread{

        @Override
        public void run() {
            db.addExistingFood();
            FirstAccess = true;
            saveData();
        }
    }

    class ExerciseThread extends Thread{

        @Override
        public void run() {
            db.addExistingExercise();
            FirstAccess = true;
            saveData();
        }
    }
}