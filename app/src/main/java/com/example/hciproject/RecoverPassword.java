package com.example.hciproject;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecoverPassword extends AppCompatActivity {

    EditText username,answer;
    TextView error,psw_label,psw;
    Button recover;
    DBHelper db;

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
        setContentView(R.layout.activity_recoverpassword);

        username = findViewById(R.id.usernametxt_recover);
        answer = findViewById(R.id.answertxt_recovery);
        error = findViewById(R.id.error_text_recover);
        psw_label = findViewById(R.id.show_password_label_recovery);
        psw = findViewById(R.id.show_password_recovery);
        recover = findViewById(R.id.recover_btn);
        db = new DBHelper(this);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString().toLowerCase();
                String ans = answer.getText().toString().toLowerCase();

                error.setVisibility(View.INVISIBLE);
                psw_label.setVisibility(View.INVISIBLE);
                psw.setVisibility(View.INVISIBLE);
                if( usr.isEmpty() || ans.isEmpty() ){
                    error.setText("Error: please fill up all the fields");
                    error.setVisibility(View.VISIBLE);
                    return;
                }

                Boolean check = db.findUser(usr);
                if (check == false){
                    error.setText("Error: Wrong Username ");
                    error.setVisibility(View.VISIBLE);
                    return;
                }

                String a = db.getUserAnswer(usr);
                if ( !a.equals(ans)){
                    error.setText("Error: Wrong Answer ");
                    error.setVisibility(View.VISIBLE);
                    return;
                }

                psw_label.setVisibility(View.VISIBLE);
                psw.setVisibility(View.VISIBLE);
                psw.setText(db.getUserPassword(usr));

            }
        });

    }
}