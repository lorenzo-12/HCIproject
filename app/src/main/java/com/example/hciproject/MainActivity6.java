package com.example.hciproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity6 extends AppCompatActivity {

    ConstraintLayout layout;
    EditText username,password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        layout = findViewById(R.id.constraintLayout);
        username = (EditText) findViewById(R.id.usernametxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        login = findViewById(R.id.loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString();
                String psw = password.getText().toString();
                if (usr.equals("admin") && (psw.equals("1234"))) layout.setBackgroundColor(Color.GREEN);
                else layout.setBackgroundColor(Color.RED);
            }
        });
    }
}