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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class UpdateExercisePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int SELECT_IMAGE = 1;

    DBHelper db;
    EditText update_name, update_reps, update_series;
    Spinner update_spinner_category;
    Button updateWorkoutbtn;
    String update_category_string;
    Boolean changes = false;
    ImageView image;
    Bitmap image_bitmap;

    String original_name,original_category,original_reps,original_series;


    @Override
    public void onBackPressed() {
        changes = false;
        Intent i = new Intent();
        Log.d("UpdateFoodPage","onBackPressed");
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
        setContentView(R.layout.activity_updateexercisepage);

        db = new DBHelper(this);
        update_name = findViewById(R.id.workout_name_text_update);
        update_spinner_category = findViewById(R.id.workout_category_spinner_update);
        update_reps = findViewById(R.id.workout_reps_text_update);
        update_series = findViewById(R.id.workout_series_text_update);
        updateWorkoutbtn = findViewById(R.id.updateWorkoutbtn);
        image = findViewById(R.id.exercisedimageview_update);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateExercisePage.this,R.array.workout_category_possible, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        update_spinner_category.setAdapter(adapter);

        update_spinner_category.setOnItemSelectedListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Update exercise Picture"), SELECT_IMAGE);
            }
        });

        updateWorkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = updateWorkout();
                //Toast.makeText(UpdateFoodPage.this,result.toString(),Toast.LENGTH_SHORT).show();
                changes = true;
                passData();
                onBackPressed();
            }
        });
        loadData();
        image.setImageBitmap(db.loadImage(original_name));
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

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        original_name = sharedPreferences.getString("workout_name","");
        original_category = sharedPreferences.getString("worout_category","other");
        original_reps = sharedPreferences.getString("workout_reps","0");
        original_series = sharedPreferences.getString("workout_series","0");
        changes = sharedPreferences.getBoolean("exercise_changes",false);

        if (update_name != null) update_name.setText(original_name);
        if (update_reps != null) update_reps.setText(original_reps);
        if (update_series != null) update_series.setText(original_series);
        if (update_category_string != null) {
            update_category_string = original_category;
            if (update_category_string.equals("chest")) update_spinner_category.setSelection(1);
            else if (update_category_string.equals("legs")) update_spinner_category.setSelection(2);
            else if (update_category_string.equals("shoulders")) update_spinner_category.setSelection(3);
            else if (update_category_string.equals("abs")) update_spinner_category.setSelection(4);
            else if (update_category_string.equals("arms")) update_spinner_category.setSelection(5);
            else if (update_category_string.equals("beck")) update_spinner_category.setSelection(6);
            else update_spinner_category.setSelection(7);
        }
    }

    public void passData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("exercise_changes",changes);
        editor.apply();
    }

    public Boolean updateWorkout(){
        String old_name = original_name;
        String new_name = update_name.getText().toString().toLowerCase();
        String category = update_category_string;
        int reps,series;
        try {
            reps = Integer.parseInt(update_reps.getText().toString());
            if (reps<0) reps = -reps;
        } catch (Exception e1){
            reps = 0;
        }
        try {
            series = Integer.parseInt(update_series.getText().toString());
            if (series<0) series = -series;
        } catch (Exception e1){
            series = 0;
        }
        Boolean check_delete = db.deleteExercise(old_name);
        if (!check_delete) return false;
        Boolean check_insert = db.addExercise(new_name,category,reps,series,image_bitmap);
        return check_insert;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        update_category_string = adapterView.getItemAtPosition(i).toString().toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        update_category_string = "other";
    }
}








