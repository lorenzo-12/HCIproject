package com.example.hciproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity6 extends AppCompatActivity {

    ConstraintLayout layout;
    EditText username,password;
    Button login;
    TextView debug,debug2;
    DB db = new DB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        layout = findViewById(R.id.constraintLayout);
        username = (EditText) findViewById(R.id.usernametxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        login = findViewById(R.id.loginbtn);
        debug = findViewById(R.id.debugtxt);
        debug2 = findViewById(R.id.debugtxt2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString();
                String psw = password.getText().toString();
                debug.setText(usr+" "+psw);
                User u = new User(usr,psw);
                db.addUser(u);
                String aux="A";
                for (User e : db.users_list) {
                    aux=aux+"A";
                }
                debug2.setText(aux);
            }
        });
    }
}