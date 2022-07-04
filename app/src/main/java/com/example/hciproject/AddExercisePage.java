package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddExercisePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String USER_TABLE = "users";
    public static final String WORKOUT_TABLE = "workout";
    public static final int SELECT_IMAGE = 1;

    DBHelper db;
    EditText input_name,input_reps,input_series;
    Spinner input_category_spinner;
    Button addExercisebtn;
    String input_category;
    Boolean changes = false;
    ImageView image;
    Bitmap image_bitmap;

    @Override
    public void onBackPressed() {
        passData();
        changes = false;
        Intent i = new Intent();
        Log.d("AddExercisePage","onBackPressed");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 0);
    }

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
        setContentView(R.layout.activity_addexercisepage);

        db = new DBHelper(this);
        input_name = findViewById(R.id.workout_name_text);
        input_category_spinner = findViewById(R.id.workout_category_spinner);
        input_reps = findViewById(R.id.workout_reps_text);
        input_series = findViewById(R.id.workout_series_text);
        addExercisebtn = findViewById(R.id.addWorkoutbtn);
        image = findViewById(R.id.exercisedimageview_add);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddExercisePage.this,R.array.workout_category_possible, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        input_category_spinner.setAdapter(adapter);

        input_category_spinner.setOnItemSelectedListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Add exercise Picture"), SELECT_IMAGE);
            }
        });

        addExercisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = addExercise();
                Toast.makeText(AddExercisePage.this,result.toString(),Toast.LENGTH_SHORT).show();
                changes = true;
                onBackPressed();
            }
        });
        image.setImageResource(R.drawable.no_image2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageuri = data.getData();
            try {
                image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                image.setImageBitmap(image_bitmap);
            } catch (IOException e) {
                image.setImageDrawable(getDrawable(R.drawable.no_image2));
            }
        }
    }

    public Boolean addExercise(){
        String name = input_name.getText().toString().toLowerCase();
        addExercisebtn.setEnabled(false);

        String error = "";
        if (name.isEmpty()) {
            Toast.makeText(AddExercisePage.this,"Please insert a name for the exercise",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (input_category.isEmpty()){
            Toast.makeText(AddExercisePage.this,"Please insert a category for the exercise",Toast.LENGTH_SHORT).show();
            return false;
        }
        addExercisebtn.setEnabled(true);

        int reps,series;
        try {
            reps = Integer.parseInt(input_reps.getText().toString());
            if (reps<0) reps = -reps;
        } catch (Exception e1){
            reps = 0;
        }
        try {
            series = Integer.parseInt(input_series.getText().toString());
            if (series<0) series = -series;
        } catch (Exception e2){
            series = 0;
        }

        Boolean check = db.findExercise(name);
        Boolean result = false;
        if (check){
            Toast.makeText(AddExercisePage.this, "exercise already exist",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            result = db.addExercise(name,input_category,reps,series,image_bitmap);
            return result;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        input_category = adapterView.getItemAtPosition(i).toString().toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        input_category = "other";
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        changes = sharedPreferences.getBoolean("exercise_changes",false);
    }

    public void passData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("exercise_changes",changes);
        editor.apply();
    }
}




