package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    EditText old_pasword, new_password,confirm_password;
    Button change_password;
    TextView error;
    DBHelper db;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*
         * without call to super onBackPress() will not be called when
         * keyCode == KeyEvent.KEYCODE_BACK
         */
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("MainActivity","onBackPressed");
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
        setContentView(R.layout.activity_changepassword);

        old_pasword = findViewById(R.id.old_password_EditText);
        new_password = findViewById(R.id.new_password_EditText);
        confirm_password = findViewById(R.id.confirm_new_password_EditText);
        change_password = findViewById(R.id.change_password_btn2);
        error = findViewById(R.id.error_text_changepassword);
        db = new DBHelper(this);

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_user_password();
            }
        });
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
    }

    public void change_user_password() {

        String old_p = old_pasword.getText().toString().toLowerCase();
        String new_p = new_password.getText().toString().toLowerCase();
        String con_p = confirm_password.getText().toString().toLowerCase();

        error.setVisibility(View.INVISIBLE);
        if (old_p.isEmpty() || new_p.isEmpty() || con_p.isEmpty()) {
            error.setText("Error: please fill up all the fileds");
            error.setVisibility(View.VISIBLE);
            return;
        }

        if (!old_p.equals(db.getUserPassword(user_logged))) {
            error.setText("Error: Wrong password");
            error.setVisibility(View.VISIBLE);
            return;
        }

        if (!new_p.equals(con_p)) {
            error.setText("Error: new Password and confirmation Password do not match");
            error.setVisibility(View.VISIBLE);
            return;
        }

        db.updateUserPassword(user_logged,new_p);
        openactivityUser();
    }

    public void openactivityUser(){
        Intent intentFitlife = new Intent(this, UserPage.class);
        startActivity(intentFitlife);
    }
}