package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExercisePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String USER_TABLE = "users";
    public static final String WORKOUT_TABLE = "workout";

    DBHelper db;
    EditText input_name,input_reps,input_series;
    Spinner input_category_spinner;
    Button addExercisebtn;
    String input_category;
    Boolean changes = false;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("AddExercisePage","onBackPressed");
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
        */
        passData();
        changes = false;
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddExercisePage.this,R.array.workout_category_possible, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_category_spinner.setAdapter(adapter);

        input_category_spinner.setOnItemSelectedListener(this);

        addExercisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = addExercise();
                Toast.makeText(AddExercisePage.this,result.toString(),Toast.LENGTH_SHORT).show();
                changes = true;
                onBackPressed();
            }
        });
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
            result = db.addExercise(name,input_category,reps,series);
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




