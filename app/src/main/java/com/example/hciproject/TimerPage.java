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
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class TimerPage extends AppCompatActivity {

    public TextView countdown_txt;
    public Button buttonStartPause, buttonReset;
    public CountDownTimer countDownTimer;
    public long startTimeMilliseconds;
    public long timeLeftMilliseconds;
    public boolean timerRunning;
    public long endTimer;
    double dec = 0;
    BottomNavigationView nav;
    ProgressBar progressBar;
    int count = 0;

    NumberPicker hh,mm,ss;

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
    protected void onResume() {
        super.onResume();
        updateCountDownText();
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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100000000);
        progressBar.setProgress(100000000);

        hh = findViewById(R.id.numpicker_hours);
        mm = findViewById(R.id.numpicker_minutes);
        ss = findViewById(R.id.numpicker_seconds);

        hh.setMaxValue(23);
        hh.setMinValue(0);
        hh.setValue(0);
        mm.setMaxValue(59);
        mm.setMinValue(0);
        mm.setValue(0);
        ss.setMaxValue(59);
        ss.setMinValue(0);
        ss.setValue(0);

        hh.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldvalue, int newvalue) {
                setTimer();
            }
        });

        mm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldvalue, int newvalue) {
                setTimer();
            }
        });

        ss.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldvalue, int newvalue) {
                setTimer();
            }
        });


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



    }

    public void setTimer(long milliseconds){
        startTimeMilliseconds = milliseconds;
        resetTimer();
        closeKeyboard();
        progressBar.setProgress(100000000);
    }

    public void startTimer(){
        hh.setEnabled(false);
        mm.setEnabled(false);
        ss.setEnabled(false);
        endTimer = System.currentTimeMillis() + timeLeftMilliseconds;
        countDownTimer = new CountDownTimer(timeLeftMilliseconds,1000) {
            @Override
            public void onTick(long l) {
                timeLeftMilliseconds = l;
                updateCountDownText();
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
        hh.setEnabled(true);
        mm.setEnabled(true);
        ss.setEnabled(true);
        countDownTimer.cancel();
        timerRunning = false;
        updateTimerInterface();
        updateCountDownText();
    }

    public void resetTimer(){
        hh.setEnabled(true);
        mm.setEnabled(true);
        ss.setEnabled(true);
        progressBar.setProgress(100000000);
        timeLeftMilliseconds = startTimeMilliseconds;
        updateTimerInterface();

        int hours = (int) ((timeLeftMilliseconds) / 1000) / 3600;
        int minutes = (int) ((timeLeftMilliseconds / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftMilliseconds / 1000) % 60;
        String format;
        format = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        countdown_txt.setText(format);
    }

    public void updateCountDownText() {
        int hours = (int) ((timeLeftMilliseconds) / 1000) / 3600;
        int minutes = (int) ((timeLeftMilliseconds / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftMilliseconds / 1000) % 60;
        String format;
        format = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        countdown_txt.setText(format);
        if (startTimeMilliseconds!=0) dec = 100000000/(startTimeMilliseconds/1000);
        else dec = 0;
        progressBar.setProgress(progressBar.getProgress()-(int) dec);
        if (((timeLeftMilliseconds/1000)%60)<1) progressBar.setProgress(0);
        //Toast.makeText(TimerPage.this, Double.toString(dec), Toast.LENGTH_SHORT).show();

    }

    public void updateTimerInterface(){
        if (timerRunning){
            buttonReset.setEnabled(false);
            buttonStartPause.setText("PAUSE");
        } else {
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

        hh.setEnabled(true);
        mm.setEnabled(true);
        ss.setEnabled(true);
        if(timerRunning) {
            endTimer = prefs.getLong("endtimer", 0);
            timeLeftMilliseconds = endTimer - System.currentTimeMillis();
            double tmp = 0;
            if (startTimeMilliseconds!=0) {
                tmp = ((double) timeLeftMilliseconds / (double) startTimeMilliseconds) * 100000000;
                progressBar.setProgress((int) tmp);
            }
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

    public void setTimer(){
        int hh_hh = hh.getValue();
        int mm_mm = mm.getValue();
        int ss_ss = ss.getValue();

        long input_hour = (long) hh_hh * 3600000;
        long input_min = (long) mm_mm * 60000;
        long input_sec = (long) ss_ss * 1000;
        long millisInput = input_hour+input_min+input_sec;
        setTimer(millisInput);
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