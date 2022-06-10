package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeGoalsPage extends AppCompatActivity {

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;

    DBHelper db;
    EditText carb,prot,fat,kal;
    Button change;

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

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
        Log.d("UserPage","onBackPressed");
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
        setContentView(R.layout.activity_changegoalspage);

        db = new DBHelper(this);
        carb = findViewById(R.id.change_carb_goal);
        prot = findViewById(R.id.change_prot_goal);
        fat = findViewById(R.id.change_fat_goal);
        kal = findViewById(R.id.change_kal_goal);

        change = findViewById(R.id.change_goals_button);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean permission = check();
                if (permission){
                    Boolean ret = changeGoals();
                    if (ret){
                        Toast.makeText(ChangeGoalsPage.this,"Success",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else Toast.makeText(ChangeGoalsPage.this,"FAILED",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(ChangeGoalsPage.this,"Please fill all the form",Toast.LENGTH_SHORT).show();
            }
        });

        loadData();
        setupGoals();
    }

    public Boolean check(){
        String c,p,f,k;
        c = carb.getText().toString();
        p = prot.getText().toString();
        f = fat.getText().toString();
        k = kal.getText().toString();

        if (c.length()==0 || p.length()==0 || f.length()==0 || k.length()==0) return false;
        else return true;
    }

    public void setupGoals(){
        Cursor cursor = db.getUserInfo(user_logged);
        if (cursor==null) return;
        cursor.moveToNext();
        String c,p,f,k;
        c = cursor.getString(5);
        p = cursor.getString(6);
        f = cursor.getString(7);
        k = cursor.getString(8);

        carb.setText(c);
        prot.setText(p);
        fat.setText(f);
        kal.setText(k);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
    }

    public Boolean changeGoals(){
        int c,p,f,k;
        c = Integer.parseInt(carb.getText().toString());
        p = Integer.parseInt(prot.getText().toString());
        f = Integer.parseInt(fat.getText().toString());
        k = Integer.parseInt(kal.getText().toString());
        return db.updateUserGoals(user_logged,c,p,f,k);
    }
}