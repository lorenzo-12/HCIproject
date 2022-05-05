package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExercisePage extends AppCompatActivity {

    public Menu menu_bar;
    public String filter = "all";
    public Boolean changes = false;

    FloatingActionButton addbtn;
    DBHelper db;
    ArrayList<String> exercise_name_list, exercise_category_list, exercise_reps_list, exercise_series_list;
    ArrayList<Bitmap> exercise_img_list;
    ConstraintLayout layout;
    RecyclerView recyclerView;
    CustomAdapterExercise customAdapterExercise;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("ExercisePage","onBackPressed");
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
        loadData();
        if (changes==false) return;
        else {
            storeDataInArrays();
            customAdapterExercise.notifyDataSetChanged();
        }
        changes = false;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercisemenu, menu);
        menu_bar = menu;

        MenuItem filter_all = menu.findItem(R.id.AllExercise_filter);
        MenuItem filter_chest = menu.findItem(R.id.Chest_filter);
        MenuItem filter_legs = menu.findItem(R.id.Legs_filter);
        MenuItem filter_shoulders = menu.findItem(R.id.Shoulders_filter);
        MenuItem filter_abs = menu.findItem(R.id.Abs_filter);
        MenuItem filter_arms = menu.findItem(R.id.Arms_filter);
        MenuItem filter_beck = menu.findItem(R.id.Beck_filter);
        MenuItem filter_other = menu.findItem(R.id.OtherExercise_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.AllExercise_filter:
                filter = "all";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Chest_filter:
                filter = "Chest";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Legs_filter:
                filter = "Legs";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Shoulders_filter:
                filter = "Shoulders";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Abs_filter:
                filter = "Abs";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Arms_filter:
                filter = "Arms";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Beck_filter:
                filter = "Beck";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.OtherExercise_filter:
                filter = "Other";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisepage);

        layout = findViewById(R.id.constraintLayout);
        recyclerView = findViewById(R.id.recycleViewWorkout);
        addbtn = findViewById(R.id.add_workout_btn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExercisePage.this, AddExercisePage.class);
                startActivity(intent);
                return;
            }
        });
        buildRecyclerView();
        storeDataInArrays();
    }

    public void storeDataInArrays(){
        Cursor cursor = null;
        if (filter.equals("all")) {
            cursor = db.readAllDataExercise();
            //Toast.makeText(ExercisePage.this,"FILTER_ALL",Toast.LENGTH_SHORT).show();
        }
        else {
            cursor = db.readFilteredExercise(filter);
            //Toast.makeText(ExercisePage.this,filter,Toast.LENGTH_SHORT).show();
        }
        exercise_name_list.clear();
        exercise_category_list.clear();
        exercise_reps_list.clear();
        exercise_series_list.clear();
        exercise_img_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            Toast.makeText(ExercisePage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            while ((cursor != null) && (cursor.moveToNext())){
                exercise_name_list.add(cursor.getString(0).toLowerCase());
                exercise_category_list.add(cursor.getString(1).toLowerCase());
                exercise_reps_list.add(cursor.getString(2).toLowerCase());
                exercise_series_list.add(cursor.getString(3).toLowerCase());
            }
        }
    }

    public void buildRecyclerView(){
        db = new DBHelper(this);
        exercise_name_list = new ArrayList<String>();
        exercise_category_list = new ArrayList<String>();
        exercise_reps_list = new ArrayList<String>();
        exercise_series_list = new ArrayList<String>();
        exercise_img_list = new ArrayList<Bitmap>();

        customAdapterExercise = new CustomAdapterExercise(ExercisePage.this, exercise_name_list, exercise_category_list, exercise_reps_list, exercise_series_list, exercise_img_list);
        recyclerView.setAdapter(customAdapterExercise);
        recyclerView.setLayoutManager(new LinearLayoutManager(ExercisePage.this));

        customAdapterExercise.setOnItemClickListener(new CustomAdapterExercise.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,"clicked: "+index,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,food_name_list.get(position).toString(),Toast.LENGTH_SHORT).show();
                Boolean result = db.deleteExercise(exercise_name_list.get(position).toLowerCase());
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
            }

            @Override
            public void onUpdateClick(int position) {
                Toast.makeText(ExercisePage.this,"Update",Toast.LENGTH_SHORT).show();
                String name = exercise_name_list.get(position).toLowerCase();
                String category = exercise_category_list.get(position).toLowerCase();
                String reps = exercise_reps_list.get(position).toLowerCase();
                String series = exercise_series_list.get(position).toLowerCase();
                passData(name,category,reps,series);
                Intent intent = new Intent(ExercisePage.this, UpdateExercisePage.class);
                startActivity(intent);
                return;
            }
        });

        customAdapterExercise.notifyDataSetChanged();
    }


    public void passData(String name, String category, String reps, String series){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("workout_name",name);
        editor.putString("workout_category",category);
        editor.putString("workout_reps",reps);
        editor.putString("workout_series",series);
        editor.putBoolean("exercise_changes",changes);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        changes = sharedPreferences.getBoolean("exercise_changes",true);
    }

}












