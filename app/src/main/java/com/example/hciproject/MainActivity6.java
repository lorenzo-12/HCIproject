package com.example.hciproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity6 extends AppCompatActivity {

    ConstraintLayout layout;
    EditText username,password;
    Button login,signup;
    TextView debug,debug2;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        db = getIntent().getParcelableExtra("DB");

        layout = findViewById(R.id.constraintLayout);
        username = (EditText) findViewById(R.id.usernametxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        login = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signupbtn);
        debug = findViewById(R.id.debugtxt);
        debug2 = findViewById(R.id.debugtxt2);

        String usr,psw;

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString();
                String psw = password.getText().toString();
                User u = new User(usr,psw);

                if (usr.equals("") || psw.equals("")){
                    if (usr.equals("")){
                        Toast.makeText(MainActivity6.this,"please enter the Username",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity6.this,"please enter the Password",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    boolean different_username = true;
                    for (User e : db.users_list) {
                        if (e.username.equals(usr)) {
                            different_username = false;
                        }
                    }

                    if (different_username == true) {
                        db.addUser(u);
                        Intent i = new Intent();
                        i.putExtra("DB",db);
                        setResult(RESULT_OK,i);
                        Toast.makeText(MainActivity6.this,"SignUp successful",Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity6.this.finish();
                            }
                        }, 500);
                    }

                    else if (different_username == false){
                        Toast.makeText(MainActivity6.this,"Username alredy used, please try another username",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString();
                String psw = password.getText().toString();
                debug.setText(usr+" "+psw);
                User u = new User(usr,psw);
                Intent i;

                if (usr.equals("") || psw.equals("")){
                    if (usr.equals("")){
                        Toast.makeText(MainActivity6.this,"please enter the Username",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity6.this,"please enter the Password",Toast.LENGTH_LONG).show();
                    }
                }

                else if (db.users_list.contains(u)){
                    i = new Intent();
                    i.putExtra("DB",db);
                    setResult(RESULT_OK,i);
                    Toast.makeText(MainActivity6.this,"login successful",Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity6.this.finish();
                        }
                    }, 500);
                }

                else {
                    Toast.makeText(MainActivity6.this,"Wrong Username or Password",Toast.LENGTH_LONG).show();
                }



                /*
                Intent i = new Intent();
                i.putExtra("DB",db);
                setResult(RESULT_OK,i);
                Toast.makeText(MainActivity6.this,"test di prova",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity6.this.finish();
                    }
                }, 5000);
                //finish();

                 */
            }
        });
    }
}