package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupInfoPage extends AppCompatActivity {

    public static final String WEIGHT_S = "SIGNUP_WEIGHT";
    public static final String HEIGHT_S = "SINGUP_HEIGHT";
    public static final String CARB_S = "SINGUP_CARB";
    public static final String PROT_S = "SINGUP_PROT";
    public static final String FAT_S = "SINGUP_FAT";
    public static final String KAL_S = "SINGUP_KAL";
    public static final String SEX_S = "SIGNUP_SEX";

    Button male,female, back,next;
    EditText weight, height,carb,prot,fat, cal;
    EditText test;
    TextView error;

    Integer sex = 0; //0: not selected, 1: male, 2: female
    Integer weight_value,height_value,carb_value,prot_value,fat_value,kal_value;

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
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        saveData();
        Log.d("FitlifePage","onBackPressed");
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
        setContentView(R.layout.activity_signupinfopage);

        error = findViewById(R.id.error_signupinfopage_text);

        weight = findViewById(R.id.signup_weight);
        height = findViewById(R.id.signup_height);
        carb = findViewById(R.id.signup_carb_goal);
        prot = findViewById(R.id.signup_prot_goal);
        fat = findViewById(R.id.signup_fat_goal);
        cal = findViewById(R.id.signup_kal_goal);

        weight.setInputType(InputType.TYPE_CLASS_NUMBER);   weight.setRawInputType(Configuration.KEYBOARD_12KEY);
        height.setInputType(InputType.TYPE_CLASS_NUMBER);   height.setRawInputType(Configuration.KEYBOARD_12KEY);
        carb.setInputType(InputType.TYPE_CLASS_NUMBER);     carb.setRawInputType(Configuration.KEYBOARD_12KEY);
        prot.setInputType(InputType.TYPE_CLASS_NUMBER);     prot.setRawInputType(Configuration.KEYBOARD_12KEY);
        fat.setInputType(InputType.TYPE_CLASS_NUMBER);      fat.setRawInputType(Configuration.KEYBOARD_12KEY);
        cal.setInputType(InputType.TYPE_CLASS_NUMBER);      cal.setRawInputType(Configuration.KEYBOARD_12KEY);


        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);
        back = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);

        loadData();

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setsex(1);
            }

        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setsex(2);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean permission = check();
                if (permission){
                    saveData();
                    openactivitySignup();
                }
            }
        });

        /*
        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (weight.getText().toString().trim().length() <= 0 )
                        weight.setError("Failed");
                    else
                        weight.setError(null);
                }
            }
        });

        height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (height.getText().toString().trim().length() <= 0 )
                        height.setError("Failed");
                    else
                        height.setError(null);
                }
            }
        });
        */

    }

    public void setsex(Integer s) {
        sex = s;
        if (s == 0){
            male.setBackgroundColor(getResources().getColor(R.color.grey));
            female.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        else if (s == 1){
            male.setBackgroundColor(getResources().getColor(R.color.celeste));
            female.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        else {
            female.setBackgroundColor(getResources().getColor(R.color.celeste));
            male.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

    public void setvalues(Integer weight_value, Integer height_value, Integer carb_value, Integer prot_value, Integer fat_value, Integer kal_value) {
        if (weight_value!=0) weight.setText(weight_value.toString());
        if (height_value!=0) height.setText(height_value.toString());
        if (carb_value!=0) carb.setText(carb_value.toString());
        if (prot_value!=0) prot.setText(prot_value.toString());
        if (fat_value!=0) fat.setText(fat_value.toString());
        if (kal_value!=0) cal.setText(kal_value.toString());
    }

    public Boolean check() {
        String v1,v2,v3,v4,v5,v6;
        v1 = weight.getText().toString();
        v2 = height.getText().toString();
        v3 = carb.getText().toString();
        v4 = prot.getText().toString();
        v5 = fat.getText().toString();
        v6 = cal.getText().toString();
        error.setVisibility(View.INVISIBLE);

        if (v1.length()==0 || v2.length()==0 || v1.equals("0") || v2.equals("0")) {
            error.setText("Please fill up all the fields");
            error.setVisibility(View.VISIBLE);
            return false;
        }
        else if (sex==0) {
            error.setText("Please fill up all the fields");
            error.setVisibility(View.VISIBLE);
            return false;
        }
        else if (v3.isEmpty() || v4.isEmpty() || v5.isEmpty() || v6.isEmpty()){
            error.setText("Please fill up all the fields");
            error.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    public void openactivitySignup(){
        saveData();
        Intent intentSignup = new Intent(this, SignupPage.class);
        startActivity(intentSignup);
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Integer w,h,c,p,f,k;
        if (weight.getText().toString().equals("")) w=0;
        else w = Integer.parseInt(weight.getText().toString());
        if (height.getText().toString().equals("")) h=0;
        else h = Integer.parseInt(height.getText().toString());
        if (carb.getText().toString().equals("")) c=0;
        else c = Integer.parseInt(carb.getText().toString());
        if (prot.getText().toString().equals("")) p=0;
        else p = Integer.parseInt(prot.getText().toString());
        if (fat.getText().toString().equals("")) f=0;
        else f = Integer.parseInt(fat.getText().toString());
        if (cal.getText().toString().equals("")) k=0;
        else k = Integer.parseInt(cal.getText().toString());

        editor.putInt(SEX_S,sex);
        editor.putInt(WEIGHT_S,w);
        editor.putInt(HEIGHT_S,h);
        editor.putInt(CARB_S,c);
        editor.putInt(PROT_S,p);
        editor.putInt(FAT_S,f);
        editor.putInt(KAL_S,k);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        sex = sharedPreferences.getInt(SEX_S,0);
        weight_value = sharedPreferences.getInt(WEIGHT_S,0);
        height_value = sharedPreferences.getInt(HEIGHT_S,0);
        carb_value = sharedPreferences.getInt(CARB_S,0);
        prot_value = sharedPreferences.getInt(PROT_S,0);
        fat_value = sharedPreferences.getInt(FAT_S,0);
        kal_value = sharedPreferences.getInt(KAL_S,0);
        setsex(sex);
        setvalues(weight_value,height_value,carb_value,prot_value,fat_value,kal_value);
        Log.d("test",sex.toString()+weight_value.toString());
    }


}