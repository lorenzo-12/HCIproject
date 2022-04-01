package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateWorkoutPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String USER_TABLE = "users";
    public static final String CNAME_WORKOUT = "workout_name";

    DBHelper db;
    EditText update_name, update_reps, update_series;
    Spinner update_spinner_category;
    Button updateWorkoutbtn;
    String update_category_string;

    String original_name,original_category,original_reps,original_series;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("UpdateFoodPage","onBackPressed");
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
        setContentView(R.layout.activity_updateworkoutpage);

        db = new DBHelper(this);
        update_name = findViewById(R.id.workout_name_text_update);
        update_spinner_category = findViewById(R.id.workout_category_spinner_update);
        update_reps = findViewById(R.id.workout_reps_text_update);
        update_series = findViewById(R.id.workout_series_text_update);
        updateWorkoutbtn = findViewById(R.id.updateWorkoutbtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateWorkoutPage.this,R.array.workout_category_possible, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_spinner_category.setAdapter(adapter);

        update_spinner_category.setOnItemSelectedListener(this);

        updateWorkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = updateWorkout();
                //Toast.makeText(UpdateFoodPage.this,result.toString(),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        loadData();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        original_name = sharedPreferences.getString("workout_name","");
        original_category = sharedPreferences.getString("worout_category","other");
        original_reps = sharedPreferences.getString("workout_reps","0");
        original_series = sharedPreferences.getString("workout_series","0");

        if (update_name != null) update_name.setText(original_name);
        if (update_reps != null) update_reps.setText(original_reps);
        if (update_series != null) update_series.setText(original_series);
        if (update_category_string != null) {
            update_category_string = original_category;
            if (update_category_string.equals("Chest")) update_spinner_category.setSelection(1);
            else if (update_category_string.equals("Legs")) update_spinner_category.setSelection(2);
            else if (update_category_string.equals("Shoulders")) update_spinner_category.setSelection(3);
            else if (update_category_string.equals("Abs")) update_spinner_category.setSelection(4);
            else if (update_category_string.equals("Arms")) update_spinner_category.setSelection(5);
            else if (update_category_string.equals("Beck")) update_spinner_category.setSelection(6);
            else update_spinner_category.setSelection(7);
        }
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
        Boolean check_delete = db.deleteWorkout(old_name);
        if (!check_delete) return false;
        Boolean check_insert = db.addWorkout(new_name,category,reps,series);
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








