package com.example.hciproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddWorkoutPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String USER_TABLE = "users";
    public static final String WORKOUT_TABLE = "workout";

    DBHelper db;
    EditText input_name,input_reps,input_series;
    Spinner input_category_spinner;
    Button addWorkoutbtn;
    String input_category;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("AddFoodPage","onBackPressed");
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
        setContentView(R.layout.activity_addworkoutpage);

        db = new DBHelper(this);
        input_name = findViewById(R.id.workout_name_text);
        input_category_spinner = findViewById(R.id.workout_category_spinner);
        input_reps = findViewById(R.id.workout_reps_text);
        input_series = findViewById(R.id.workout_series_text);
        addWorkoutbtn = findViewById(R.id.addWorkoutbtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddWorkoutPage.this,R.array.workout_category_possible, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_category_spinner.setAdapter(adapter);

        input_category_spinner.setOnItemSelectedListener(this);

        addWorkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = addWorkout();
                Toast.makeText(AddWorkoutPage.this,result.toString(),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    public Boolean addWorkout(){
        String name = input_name.getText().toString().toLowerCase();
        addWorkoutbtn.setEnabled(false);

        String error = "";
        if (name.isEmpty()) {
            Toast.makeText(AddWorkoutPage.this,"Please insert a name for the workout",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (input_category.isEmpty()){
            Toast.makeText(AddWorkoutPage.this,"Please insert a category for the workout",Toast.LENGTH_SHORT).show();
            return false;
        }
        addWorkoutbtn.setEnabled(true);

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

        Boolean check = db.findWorkout(name);
        Boolean result = false;
        if (check){
            Toast.makeText(AddWorkoutPage.this, "Workout already exist",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            result = db.addWorkout(name,input_category,reps,series);
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
}




