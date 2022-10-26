package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupPage extends AppCompatActivity {

    /* public static final String WEIGHT_S = "SIGNUP_WEIGHT";
    public static final String HEIGHT_S = "SINGUP_HEIGHT";
    public static final String CARB_S = "SINGUP_CARB";
    public static final String PROT_S = "SINGUP_PROT";
    public static final String FAT_S = "SINGUP_FAT";
    public static final String KAL_S = "SINGUP_KAL";
    public static final String SEX_S = "SIGNUP_SEX";
    public static final String USER_LOGGED = "user_logged";
    public String user_logged; */


    /* Integer sex = 0; //0: not selected, 1: male, 2: female
    Integer weight_value,height_value,carb_value,prot_value,fat_value,kal_value; */

    //EditText username,password,confirm,answer;
    //Button signup,back;
    //DBHelper db;
    //TextView password_error,error;
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;

    //codice per fare si che quando un utente clicca fuori da un Edittext si perde il focus
    /* @Override
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
        loadData();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("SignupInfoPage","onBackPressed");
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
        setContentView(R.layout.activity_signuppage);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        loadData();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usr = username.getText().toString().toLowerCase();
                String psw = password.getText().toString().toLowerCase();
                String cnf = confirm.getText().toString().toLowerCase();
                String ans = answer.getText().toString().toLowerCase();

                error.setVisibility(View.INVISIBLE);
                password_error.setVisibility(View.INVISIBLE);
                Boolean ret = false;
                if (usr.isEmpty() || psw.isEmpty() || cnf.isEmpty() || ans.isEmpty()) {
                    error.setVisibility(View.VISIBLE);
                    ret = true;
                }

                if ( !psw.equals(cnf) ){
                    password_error.setVisibility(View.VISIBLE);
                    ret = true;
                }
                if (ret) return;

                Boolean check;
                try {
                    Bitmap img = ((BitmapDrawable)getResources().getDrawable(R.drawable.no_image2)).getBitmap();
                    check = db.addUser(usr,psw,img,weight_value,height_value,sex,carb_value,prot_value,fat_value,kal_value,ans);
                } catch (Exception e){
                    check = db.addUser(usr,psw,weight_value,height_value,sex,carb_value,prot_value,fat_value,kal_value,ans);
                }

                if (check){
                    //Toast.makeText(SignupPage.this,"OK",Toast.LENGTH_SHORT).show();
                    user_logged = usr;
                    saveData();
                    openactivityMain();
                }else {
                    //Toast.makeText(SignupPage.this,"FAIL",Toast.LENGTH_SHORT).show();
                }
                String res = db.viewUsers();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_LOGGED,user_logged);
        editor.putInt(SEX_S,sex);
        editor.putInt(WEIGHT_S,0);
        editor.putInt(HEIGHT_S,0);
        editor.putInt(CARB_S,0);
        editor.putInt(PROT_S,0);
        editor.putInt(FAT_S,0);
        editor.putInt(KAL_S,0);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
        sex = sharedPreferences.getInt(SEX_S,0);
        weight_value = sharedPreferences.getInt(WEIGHT_S,0);
        height_value = sharedPreferences.getInt(HEIGHT_S,0);
        carb_value = sharedPreferences.getInt(CARB_S,0);
        prot_value = sharedPreferences.getInt(PROT_S,0);
        fat_value = sharedPreferences.getInt(FAT_S,0);
        kal_value = sharedPreferences.getInt(KAL_S,0);
    }

    public void openactivityMain(){
        saveData();
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(SignupPage.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupPage.this , SignupInfoPage.class));
                    finish();
                } else if (txt_password.length() < 6){
                    Toast.makeText(SignupPage.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email , txt_password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupPage.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignupPage.this, "Registering user successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupPage.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}