package com.example.hciproject;

import android.content.Intent;
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

public class FoodPage extends AppCompatActivity {

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";

    public static final String CNAME_FOOD = "food_name";
    public static final String CCATEGORY_FOOD = "food_category";
    public static final String CCARB = "carb";
    public static final String CPROT = "prot";
    public static final String CFAT = "fat";

    FloatingActionButton addbtn;
    DBHelper db;
    ArrayList<String> food_name_list, food_category_list, food_carb_list, food_prot_list, food_fat_list;
    ConstraintLayout layout;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("FoodPage","onBackPressed");
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
        customAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodpage);

        layout = findViewById(R.id.constraintLayout);
        recyclerView = findViewById(R.id.recycleView);
        addbtn = findViewById(R.id.add_food_btn);
        buildRecyclerView();

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodPage.this, AddFoodPage.class);
                startActivity(intent);
                return;
            }
        });


        storeDataInArrays();
    }


    public void storeDataInArrays(){
        Cursor cursor = db.readAllDataFood();
        food_name_list.clear();
        food_category_list.clear();
        food_carb_list.clear();
        food_prot_list.clear();
        food_fat_list.clear();
        if (cursor.getCount() == 0){
            Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                food_name_list.add(cursor.getString(0).toLowerCase());
                food_category_list.add(cursor.getString(1).toLowerCase());
                food_carb_list.add("Carb: "+cursor.getString(2).toLowerCase());
                food_prot_list.add("Prot: "+cursor.getString(3).toLowerCase());
                food_fat_list.add("Fat:  "+cursor.getString(4).toLowerCase());
            }
        }

    }

    public void buildRecyclerView(){
        db = new DBHelper(this);
        food_name_list = new ArrayList<String>();
        food_category_list = new ArrayList<String>();
        food_carb_list = new ArrayList<String>();
        food_prot_list = new ArrayList<String>();
        food_fat_list = new ArrayList<String>();

        customAdapter = new CustomAdapter(FoodPage.this,food_name_list, food_category_list, food_carb_list, food_prot_list, food_fat_list);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodPage.this));

        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,"clicked: "+index,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,food_name_list.get(position).toString(),Toast.LENGTH_SHORT).show();
                Boolean result = db.deleteFood(food_name_list.get(position).toString().toLowerCase());
                storeDataInArrays();
                customAdapter.notifyDataSetChanged();
            }
        });

        customAdapter.notifyDataSetChanged();
    }

}