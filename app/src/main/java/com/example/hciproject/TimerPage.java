package com.example.hciproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TimerPage extends AppCompatActivity {

    public TextView countdown_txt;
    public EditText timer_input_txt;
    public Button countdown_button,reset_button,timer_input_btn;
    public CountDownTimer countDownTimer;
    public long timeStartMilliseconds;
    public long timeLeftMilliseconds;
    public boolean timerRunning;
    public long endTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timerpage);

        countdown_txt = findViewById(R.id.countdown_text);
        countdown_button = findViewById(R.id.countdown_btn);
        reset_button = findViewById(R.id.countdown_reset_btn);
        timer_input_txt = findViewById(R.id.timer_input_text);
        timer_input_btn = findViewById(R.id.timer_input_btn);

        timer_input_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String input = timer_input_txt.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(TimerPage.this,"Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long inputInMillis = Long.parseLong(input) * 60000;
                if (inputInMillis == 0){
                    Toast.makeText(TimerPage.this,"Please enter a positive number",Toast.LENGTH_SHORT).show();
                    return;
                }

                setTimer(inputInMillis);
                timer_input_txt.setText("");
            }


        });

        countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateTimer();
    }

    public void resetTimer(){
        timeLeftMilliseconds = timeStartMilliseconds;
        updateTimer();
        updateTimerInterface();
    }

    public void startStop(){
        if (timerRunning){
            stopTimer();
        }
        else {
            startTimer();
        }
    }

    public void setTimer(long milliseconds){
        timeStartMilliseconds = milliseconds;
        resetTimer();
    }

    public void startTimer(){

        endTimer = System.currentTimeMillis() + timeLeftMilliseconds;

        countDownTimer = new CountDownTimer(timeLeftMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                countdown_button.setText("START");
                countdown_button.setEnabled(false);
                reset_button.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
        updateTimer();
        updateTimerInterface();
    }

    public void stopTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        updateTimerInterface();
    }

    public void updateTimer(){
        int hours = (int) (timeLeftMilliseconds / 1000) / 3600;
        int minutes = (int) ((timeLeftMilliseconds / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftMilliseconds / 1000) % 60;

        String timeLeftText;
        if (hours > 0 ){
            timeLeftText = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        }
        else {
            timeLeftText = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        }

        countdown_txt.setText(timeLeftText);

    }

    public void updateTimerInterface(){
        if (timerRunning){
            timer_input_txt.setEnabled(false);
            timer_input_btn.setEnabled(false);
            reset_button.setVisibility(View.INVISIBLE);
            countdown_button.setText("PAUSE");
        }
        else {
            timer_input_txt.setEnabled(true);
            timer_input_btn.setEnabled(true);
            countdown_button.setText("START");
            if (timeLeftMilliseconds < 1000){
                countdown_button.setEnabled(false);
            }
            else {
                countdown_button.setEnabled(true);
            }

            if (timeLeftMilliseconds < timeStartMilliseconds){
                reset_button.setVisibility(View.VISIBLE);
            }
            else {
                reset_button.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("rimerStartMilliseconds",timeStartMilliseconds);
        editor.putLong("timeLeftMilliseconds",timeLeftMilliseconds);
        editor.putLong("endTimer",endTimer);
        editor.putBoolean("timerRunning",timerRunning);

        editor.apply();
        countDownTimer.cancel();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY",MODE_PRIVATE);

        timeStartMilliseconds = sharedPreferences.getLong("timeStartMilliseconds",120000);
        timeLeftMilliseconds = sharedPreferences.getLong("timeLeftMilliseconds", timeStartMilliseconds);
        timerRunning = sharedPreferences.getBoolean("timerRunning",false);
        updateTimer();
        updateTimerInterface();

        if (timerRunning){
            endTimer = sharedPreferences.getLong("endTimer",0);
            timeLeftMilliseconds = endTimer-System.currentTimeMillis();

            if (timeLeftMilliseconds < 0){
                timeLeftMilliseconds = 0;
                timerRunning = false;
                updateTimer();
                updateTimerInterface();
            }
            else {
                startTimer();
            }
        }
    }
}