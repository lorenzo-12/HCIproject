package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class TimerPage extends AppCompatActivity {

    public TextView countdown_txt;
    public EditText hours,minutes,seconds;
    public Button buttonStartPause, buttonReset, buttonSet, test;
    public CountDownTimer countDownTimer;
    public long startTimeMilliseconds;
    public long timeLeftMilliseconds;
    public boolean timerRunning;
    public long endTimer;
    BottomNavigationView nav;
    ProgressBar progressBar;
    int count = 0;

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
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("TimerPage","onBackPressed");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timerpage);


        countdown_txt = findViewById(R.id.countdown_text);
        buttonStartPause = findViewById(R.id.countdown_btn);
        buttonReset = findViewById(R.id.countdown_reset_btn);
        minutes = findViewById(R.id.timer_input_text_minutes);
        seconds = findViewById(R.id.timer_input_text_seconds);
        hours = findViewById(R.id.timer_input_text_hours);
        buttonSet = findViewById(R.id.timer_input_btn);
        test = findViewById(R.id.buttonprogressbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(100);


        nav = findViewById(R.id.bottomnavigatorviewTimer);
        //così quando apro l'app mi da fin  da subito selezionata l'icona del cibo
        nav.setSelectedItemId(R.id.bottom_time);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_diary:
                        openactivityDiary();
                        return true;
                    case R.id.bottom_exercise:
                        Toast.makeText(TimerPage.this,"exercise",Toast.LENGTH_SHORT).show();
                        openactivityexercise();
                        return true;
                    case R.id.bottom_food:
                        Toast.makeText(TimerPage.this,"food",Toast.LENGTH_SHORT).show();
                        openactivityFood();
                        return true;
                    case R.id.bottom_time:
                        //do nothing, we are already in the time page
                        return true;
                    case R.id.bottom_user:
                        Toast.makeText(TimerPage.this,"user",Toast.LENGTH_SHORT).show();
                        openactivityuser();
                        return true;
                }
                return true;
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(progressBar.getProgress()-10);
            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_hours = hours.getText().toString();
                String input_minutes = minutes.getText().toString();
                String input_seconds = seconds.getText().toString();
                input_hours = "1";
                if (input_minutes.length() == 0 || input_seconds.length()==0 || input_hours.length()==0){
                    Toast.makeText(TimerPage.this,"Field can't be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                long input_hour = Long.parseLong(input_hours) * 3600000;
                long input_min = Long.parseLong(input_minutes) * 60000;
                long input_sec = Long.parseLong(input_seconds) * 1000;
                long millisInput = input_min+input_sec;
                if (millisInput < 0){
                    Toast.makeText(TimerPage.this,"Please enter a positive number",Toast.LENGTH_SHORT).show();
                    return;
                }
                setTimer(millisInput);
                hours.setText("");
                minutes.setText("");
                seconds.setText("");
            }
        });

       buttonStartPause.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                if (timerRunning){
                    pauseTimer();
                } else {
                    startTimer();
                }
           }
       });

       buttonReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                resetTimer();
           }
       });

       updateCountDownText();

       seconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean b) {
               if (seconds.getText().toString().length()==0) return;
               long tmp = Long.parseLong(seconds.getText().toString());
               if (tmp>=60) seconds.setText("59");
           }
       });

        minutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (minutes.getText().toString().length()==0) return;
                long tmp = Long.parseLong(minutes.getText().toString());
                if (tmp>=60) minutes.setText("59");
            }
        });

        hours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (hours.getText().toString().length()==0) return;
                long tmp = Long.parseLong(hours.getText().toString());
                if (tmp>=24) hours.setText("23");
            }
        });

    }

    public void setTimer(long milliseconds){
        startTimeMilliseconds = milliseconds;
        resetTimer();
        closeKeyboard();
        progressBar.setProgress(100);
    }

    public void startTimer(){
        endTimer = System.currentTimeMillis() + timeLeftMilliseconds;
        countDownTimer = new CountDownTimer(timeLeftMilliseconds,1000) {
            @Override
            public void onTick(long l) {
                timeLeftMilliseconds = l;
                updateCountDownText();
                double start = (double) startTimeMilliseconds;
                double left = (double) timeLeftMilliseconds;
                double perc = 0;
                if (timeLeftMilliseconds!=0) {
                    perc =  (left / start) * 100;
                }
                progressBar.setProgress((int) perc);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                updateTimerInterface();
            }
        }.start();

        timerRunning = true;
        updateTimerInterface();


    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        updateTimerInterface();
    }

    public void resetTimer(){
        timeLeftMilliseconds = startTimeMilliseconds;
        updateCountDownText();
        updateTimerInterface();
    }

    public void updateCountDownText() {
        int hours = (int) ((timeLeftMilliseconds) / 1000) / 36000;
        int minutes = (int) ((timeLeftMilliseconds / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftMilliseconds / 1000) % 60;
        String format;
        if (hours >= 0){
            format = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            format = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        countdown_txt.setText(format);

    }

    public void updateTimerInterface(){
        if (timerRunning){
            minutes.setEnabled(false);
            buttonSet.setEnabled(false);
            buttonReset.setEnabled(false);
            buttonStartPause.setText("PAUSE");
        } else {
            minutes.setEnabled(true);
            buttonSet.setEnabled(true);
            buttonStartPause.setText("START");
            if (timeLeftMilliseconds < 1000){
                buttonStartPause.setEnabled(false);
            }else {
                buttonStartPause.setEnabled(true);
            }

            if (timeLeftMilliseconds < startTimeMilliseconds){
                buttonReset.setEnabled(true);
            }else {
                buttonReset.setEnabled(false);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("ALL_ACTIVITY",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisecondsleft",timeLeftMilliseconds);
        editor.putBoolean("timerrunning",timerRunning);
        editor.putLong("endtimer",endTimer);
        editor.putLong("starttimemillies",startTimeMilliseconds);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("ALL_ACTIVITY",MODE_PRIVATE);
        timeLeftMilliseconds = prefs.getLong("millisecondsleft",startTimeMilliseconds);
        timerRunning = prefs.getBoolean("timerrunning",false);
        startTimeMilliseconds = prefs.getLong("starttimemillies",0);

        updateTimerInterface();
        updateCountDownText();
        if(timerRunning) {
            endTimer = prefs.getLong("endtimer", 0);
            timeLeftMilliseconds = endTimer - System.currentTimeMillis();
            if(timeLeftMilliseconds < 0){
                timeLeftMilliseconds = 0;
                timerRunning = false;
                updateTimerInterface();
                updateCountDownText();
            }else {
                startTimer();
            }
        }


    }

    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public void openactivityDiary(){
        Intent intentDiary = new Intent(this, MainActivity.class);
        startActivity(intentDiary);
    }

    public void openactivityFood(){
        Intent intentDiet = new Intent(this, FoodPage.class);
        startActivity(intentDiet);
    }

    public void openactivityexercise(){
        Intent intentExercise = new Intent(this, ExercisePage.class);
        startActivity(intentExercise);
    }

    public void openactivityuser() {
        Intent intentUser;
        //creo la nuova pagina (intentUser)
        intentUser = new Intent(this, UserPage.class);
        //se non mi interessa ricevere delle informazioni dalla pagina figlia allora posso usare
        //direttamente STARTACTIVITY
        startActivity(intentUser);

    }
}