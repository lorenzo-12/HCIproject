package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FitlifePage extends AppCompatActivity {

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    Boolean FirstAccess = false;

    Button login,signup;
    DBHelper db;

    //codice per fare si che quando un utente clicca fuori da un Edittext si perde il focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitlifepage);

        login = findViewById(R.id.fitlife_login_button);
        signup = findViewById(R.id.fitlife_signup_button);
        db = new DBHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityLogin();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivitySignup();
            }
        });

        loadData();

        if (!user_logged.equals("none")) {
            saveData();
            openactivityMain();
        }

        if (FirstAccess == false) {
            startThreadFood();
            startThreadExercise();
        }
    }

    public void openactivityLogin(){
        saveData();
        Intent intentLogin = new Intent(this, LoginPage.class);
        startActivity(intentLogin);
    }

    public void openactivitySignup(){
        saveData();
        Intent intentSignup = new Intent(this, SignupInfoPage.class);
        startActivity(intentSignup);
    }

    public void openactivityMain(){
        saveData();
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
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
        FitlifePage.FoodThread t = new FitlifePage.FoodThread();
        t.start();
    }

    public void startThreadExercise(){
        FitlifePage.ExerciseThread t = new FitlifePage.ExerciseThread();
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