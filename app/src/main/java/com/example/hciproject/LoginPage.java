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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

    public static final String LIGHTMODE = "lightmode";
    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";
    public static final String USER_LOGGED = "user_logged";

    public String user_logged;
    public Boolean lightmode;

    EditText username,password;
    DBHelper db;
    Button login;


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
        Log.d("FitlifePage","onBackPressed");
        saveData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        username = findViewById(R.id.usernametxt_login);
        password = findViewById(R.id.passwordtxt_login);
        login = findViewById(R.id.login_button);

        db = new DBHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString().toLowerCase();
                String psw = password.getText().toString().toLowerCase();

                if (usr.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(LoginPage.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                Boolean check = db.checkUser(usr,psw);
                if (check){
                    Toast.makeText(LoginPage.this,"OK",Toast.LENGTH_SHORT).show();
                    user_logged = usr;
                }else {
                    Toast.makeText(LoginPage.this,"FAIL",Toast.LENGTH_SHORT).show();
                }
                String res = db.viewUsers();
                saveData();
                openactivityMain();
            }
        });
        loadData();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGGED,user_logged);
        editor.putBoolean(LIGHTMODE,lightmode);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        lightmode = sharedPreferences.getBoolean(LIGHTMODE,true);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
    }

    public void openactivityMain(){
        saveData();
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
}