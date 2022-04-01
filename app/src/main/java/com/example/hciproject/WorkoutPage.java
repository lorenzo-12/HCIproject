package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WorkoutPage extends AppCompatActivity {

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";

    public static final String WORKOUT_TABLE = "workout";
    public static final String CNAME_WORKOUT = "workout_name";
    public static final String CCATEGORY_WORKOUT = "workout_category";
    public static final String CREPS_WORKOUT = "workout_reps";
    public static final String CSERIES_WORKOUT = "workout_series";

    FloatingActionButton addbtn;
    DBHelper db;
    ArrayList<String> workout_name_list, workout_category_list, workout_reps_list, workout_series_list;
    ConstraintLayout layout;
    RecyclerView recyclerView;
    CustomAdapterWorkout customAdapterWorkout;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("WorkoutPage","onBackPressed");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(FoodPage.this,"resumed",Toast.LENGTH_SHORT).show();
        storeDataInArrays();
        customAdapterWorkout.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workoutpage);

        layout = findViewById(R.id.constraintLayout);
        recyclerView = findViewById(R.id.recycleViewWorkout);
        addbtn = findViewById(R.id.add_workout_btn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutPage.this, AddWorkoutPage.class);
                startActivity(intent);
                return;
            }
        });
        buildRecyclerView();
        storeDataInArrays();
    }

    public void storeDataInArrays(){
        Cursor cursor = db.readAllDataWorkout();
        workout_name_list.clear();
        workout_category_list.clear();
        workout_reps_list.clear();
        workout_series_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            Toast.makeText(WorkoutPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            while ((cursor != null) && (cursor.moveToNext())){
                workout_name_list.add(cursor.getString(0).toLowerCase());
                workout_category_list.add(cursor.getString(1).toLowerCase());
                workout_reps_list.add(cursor.getString(2).toLowerCase());
                workout_series_list.add(cursor.getString(3).toLowerCase());
            }
        }
    }

    public void buildRecyclerView(){
        db = new DBHelper(this);
        workout_name_list = new ArrayList<String>();
        workout_category_list = new ArrayList<String>();
        workout_reps_list = new ArrayList<String>();
        workout_series_list = new ArrayList<String>();

        customAdapterWorkout = new CustomAdapterWorkout(WorkoutPage.this,workout_name_list, workout_category_list, workout_reps_list, workout_series_list);
        recyclerView.setAdapter(customAdapterWorkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutPage.this));

        customAdapterWorkout.setOnItemClickListener(new CustomAdapterWorkout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,"clicked: "+index,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,food_name_list.get(position).toString(),Toast.LENGTH_SHORT).show();
                Boolean result = db.deleteFood(workout_name_list.get(position).toLowerCase());
                storeDataInArrays();
                customAdapterWorkout.notifyDataSetChanged();
            }

            @Override
            public void onUpdateClick(int position) {
                Toast.makeText(WorkoutPage.this,"Update",Toast.LENGTH_SHORT).show();
                String name = workout_name_list.get(position).toLowerCase();
                String category = workout_category_list.get(position).toLowerCase();
                String reps = workout_reps_list.get(position).toLowerCase();
                String series = workout_series_list.get(position).toLowerCase();
                passFoodData(name,category,reps,series);
                Intent intent = new Intent(WorkoutPage.this, UpdateWorkoutPage.class);
                startActivity(intent);
                return;
            }
        });

        customAdapterWorkout.notifyDataSetChanged();
    }


    public void passFoodData(String name,String category,String reps, String series){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("workout_name",name);
        editor.putString("workout_category",category);
        editor.putString("workout_reps",reps);
        editor.putString("workout_series",series);
        editor.apply();
    }

}












