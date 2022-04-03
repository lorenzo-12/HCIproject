package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";

    public static final String WORKOUT_TABLE = "workout";
    public static final String CNAME_WORKOUT = "workout_name";
    public static final String CCATEGORY_WORKOUT = "workout_category";
    public static final String CREPS_WORKOUT = "workout_reps";
    public static final String CSERIES_WORKOUT = "workout_series";
    public Menu menu_bar;
    public String filter = "all";

    FloatingActionButton addbtn;
    DBHelper db;
    ArrayList<String> workout_name_list, workout_category_list, workout_reps_list, workout_series_list;
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
        storeDataInArrays();
        customAdapterExercise.notifyDataSetChanged();
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
                filter = "chest";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Legs_filter:
                filter = "legs";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Shoulders_filter:
                filter = "shoulders";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Abs_filter:
                filter = "abs";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Arms_filter:
                filter = "arms";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.Beck_filter:
                filter = "beck";
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
                return true;
            case R.id.OtherExercise_filter:
                filter = "other";
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
        workout_name_list.clear();
        workout_category_list.clear();
        workout_reps_list.clear();
        workout_series_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            Toast.makeText(ExercisePage.this,"No Data",Toast.LENGTH_SHORT).show();
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

        customAdapterExercise = new CustomAdapterExercise(ExercisePage.this,workout_name_list, workout_category_list, workout_reps_list, workout_series_list);
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
                Boolean result = db.deleteWorkout(workout_name_list.get(position).toLowerCase());
                storeDataInArrays();
                customAdapterExercise.notifyDataSetChanged();
            }

            @Override
            public void onUpdateClick(int position) {
                Toast.makeText(ExercisePage.this,"Update",Toast.LENGTH_SHORT).show();
                String name = workout_name_list.get(position).toLowerCase();
                String category = workout_category_list.get(position).toLowerCase();
                String reps = workout_reps_list.get(position).toLowerCase();
                String series = workout_series_list.get(position).toLowerCase();
                passFoodData(name,category,reps,series);
                Intent intent = new Intent(ExercisePage.this, UpdateExercisePage.class);
                startActivity(intent);
                return;
            }
        });

        customAdapterExercise.notifyDataSetChanged();
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












