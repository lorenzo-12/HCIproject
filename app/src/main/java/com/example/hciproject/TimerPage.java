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
    public EditText editTextInput;
    public Button buttonStartPause, buttonReset, buttonSet;
    public CountDownTimer countDownTimer;
    public long startTimeMilliseconds;
    public long timeLeftMilliseconds;
    public boolean timerRunning;
    public long endTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timerpage);


        countdown_txt = findViewById(R.id.countdown_text);
        buttonStartPause = findViewById(R.id.countdown_btn);
        buttonReset = findViewById(R.id.countdown_reset_btn);
        editTextInput = findViewById(R.id.timer_input_text);
        buttonSet = findViewById(R.id.timer_input_btn);

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editTextInput.getText().toString();
                if (input.length() == 0){
                    Toast.makeText(TimerPage.this,"Field can't be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0){
                    Toast.makeText(TimerPage.this,"Please enter a positive number",Toast.LENGTH_SHORT).show();
                    return;
                }
                setTimer(millisInput);
                editTextInput.setText("");
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
    }

    public void startTimer(){
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
        if (hours > 0){
            format = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            format = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        countdown_txt.setText(format);
    }

    public void updateTimerInterface(){
        if (timerRunning){
            editTextInput.setEnabled(false);
            buttonSet.setEnabled(false);
            buttonReset.setEnabled(false);
            buttonStartPause.setText("PAUSE");
        } else {
            editTextInput.setEnabled(true);
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
        startTimeMilliseconds = prefs.getLong("starttimemillies",120000);

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
}