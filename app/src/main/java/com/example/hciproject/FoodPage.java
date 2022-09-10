package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FoodPage extends AppCompatActivity {

    public Menu menu_bar;
    public String filter = "all";
    public Integer filter_type = 0;
    public Boolean changes = false;

    DBHelper db;
    ArrayList<String> food_name_list, food_category_list, food_carb_list, food_prot_list, food_fat_list, food_kal_list;
    ArrayList<Bitmap> food_img_list;
    RecyclerView recyclerView;
    CustomAdapterFood customAdapterFood;
    BottomNavigationView nav;
    AutoCompleteTextView search;
    ArrayAdapter adapter;
    FloatingActionButton add_food_btn;

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
        loadData();
        if (changes==false) return;
        else {
            storeDataInArrays();
            customAdapterFood.notifyDataSetChanged();
        }
        changes = false;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.foodmenu, menu);
        menu_bar = menu;

        MenuItem filter_all = menu.findItem(R.id.AllFood_filter);
        MenuItem filter_fruit = menu.findItem(R.id.FruitVegetable_filter);
        MenuItem filter_cereal = menu.findItem(R.id.Cereal_filter);
        MenuItem filter_dairy = menu.findItem(R.id.Dairy_filter);
        MenuItem filter_meat = menu.findItem(R.id.MeatFishEgg_filter);
        MenuItem filter_sweet = menu.findItem(R.id.Sweet_filter);
        MenuItem filter_other = menu.findItem(R.id.OtherFood_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.AllFood_filter:
                filter = "all";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            case R.id.FruitVegetable_filter:
                filter = "FruitVegetable";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            case R.id.Cereal_filter:
                filter = "Cereal";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            case R.id.Dairy_filter:
                filter = "Dairy";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            case R.id.MeatFishEgg_filter:
                filter = "MeatFisheggs";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            case R.id.Sweet_filter:
                filter = "Sweet";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            case R.id.OtherFood_filter:
                filter = "Other";
                filter_type = 0;
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodpage);

        recyclerView = findViewById(R.id.recycleViewFood);
        search = findViewById(R.id.search_food_text);
        adapter = (ArrayAdapter<String>) new ArrayAdapter<String>(this, R.layout.autocomlete_layout,food_name_list);
        search.setAdapter(adapter);
        add_food_btn = findViewById(R.id.add_food_btn_foodpage);
        //search.setDropDownBackgroundResource(R.color.white);

        nav = findViewById(R.id.bottomnavigatorviewFood);
        //così quando apro l'app mi da fin  da subito selezionata l'icona del cibo
        nav.setSelectedItemId(R.id.bottom_food);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_diary:
                        openactivityDiary();
                        return true;
                    case R.id.bottom_exercise:
                        //Toast.makeText(FoodPage.this,"exercise",Toast.LENGTH_SHORT).show();
                        openactivityexercise();
                        return true;
                    case R.id.bottom_food:
                        //Toast.makeText(FoodPage.this,"food",Toast.LENGTH_SHORT).show();
                        //do nothing since we are already in the Diary activity
                        return true;
                    case R.id.bottom_time:
                        //Toast.makeText(FoodPage.this,"timer",Toast.LENGTH_SHORT).show();
                        openactivitytimer();
                        return true;
                    case R.id.bottom_user:
                        //Toast.makeText(FoodPage.this,"user",Toast.LENGTH_SHORT).show();
                        openactivityuser();
                        return true;
                }
                return true;
            }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //Toast.makeText(FoodPage.this,"aaa",Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return false;
            }
        });

        add_food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodPage.this, AddFoodPage.class);
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //non mi serve
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                filter=search.getText().toString().toLowerCase();
                filter_type=1;
                if (filter.equals("")) {
                    filter="all";
                    filter_type=0;
                }
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //non mi serve
            }
        });



        buildRecyclerView();
        storeDataInArrays();
    }


    public void storeDataInArrays(){
        Cursor cursor = null;
        if (filter_type==0) {
            if (filter.equals("all")) {
                cursor = db.readAllDataFood();
                //Toast.makeText(FoodPage.this,"FILTER_ALL",Toast.LENGTH_SHORT).show();
            } else {
                cursor = db.readFilteredFoodByCategory(filter);
                //Toast.makeText(FoodPage.this, filter, Toast.LENGTH_SHORT).show();
            }
        } else if (filter_type==1){
            cursor = db.readFilteredFoodByName(filter);
        }
        food_name_list.clear();
        food_category_list.clear();
        food_carb_list.clear();
        food_prot_list.clear();
        food_fat_list.clear();
        food_img_list.clear();
        food_kal_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            //Toast.makeText(FoodPage.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(FoodPage.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            while ((cursor != null) && (cursor.moveToNext())){
                food_name_list.add(cursor.getString(0).toLowerCase());
                food_category_list.add(cursor.getString(1).toLowerCase());
                food_carb_list.add(cursor.getString(2).toLowerCase());
                food_prot_list.add(cursor.getString(3).toLowerCase());
                food_fat_list.add(cursor.getString(4).toLowerCase());
                food_kal_list.add(cursor.getString(5).toLowerCase());
            }
        }
        adapter = (ArrayAdapter<String>) new ArrayAdapter<String>(this, R.layout.autocomlete_layout,food_name_list);
        search.setAdapter(adapter);

    }

    public void buildRecyclerView(){
        db = new DBHelper(this);
        food_name_list = new ArrayList<String>();
        food_category_list = new ArrayList<String>();
        food_carb_list = new ArrayList<String>();
        food_prot_list = new ArrayList<String>();
        food_fat_list = new ArrayList<String>();
        food_kal_list = new ArrayList<String>();
        food_img_list = new ArrayList<Bitmap>();

        customAdapterFood = new CustomAdapterFood(FoodPage.this,food_name_list, food_category_list, food_carb_list, food_prot_list, food_fat_list,food_kal_list, food_img_list);
        recyclerView.setAdapter(customAdapterFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodPage.this));

        customAdapterFood.setOnItemClickListener(new CustomAdapterFood.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,"clicked: "+index,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,food_name_list.get(position).toString(),Toast.LENGTH_SHORT).show();
                Boolean result = db.deleteFood(food_name_list.get(position).toLowerCase());
                storeDataInArrays();
                customAdapterFood.notifyDataSetChanged();
            }

            @Override
            public void onUpdateClick(int position) {
                //Toast.makeText(FoodPage.this,"Update",Toast.LENGTH_SHORT).show();
                String name = food_name_list.get(position).toLowerCase();
                String category = food_category_list.get(position).toLowerCase();
                String carb = food_carb_list.get(position).toLowerCase();
                String prot = food_prot_list.get(position).toLowerCase();
                String fat = food_fat_list.get(position).toLowerCase();
                String kal = food_kal_list.get(position).toLowerCase();
                passData(name,category,carb,prot,fat,kal);
                Intent intent = new Intent(FoodPage.this, UpdateFoodPage.class);
                startActivity(intent);
                return;
            }
        });

        customAdapterFood.notifyDataSetChanged();
    }

    public void passData(String name, String category, String carb, String prot, String fat,String kal){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("food_name",name);
        editor.putString("food_category",category);
        editor.putString("food_carb",carb);
        editor.putString("food_prot",prot);
        editor.putString("food_fat",fat);
        editor.putString("food_kal",kal);
        editor.putBoolean("food_changes",changes);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        changes = sharedPreferences.getBoolean("food_changes",true);
    }

    //relative funzioni che vengono chiamete quando premiamo un bottone

    public void openactivityexercise(){
        Intent intentWorkout = new Intent(this, ExercisePage.class);
        startActivity(intentWorkout);
    }

    public void openactivitytimer(){
        Intent intentTimer = new Intent(this, TimerPage.class);
        startActivity(intentTimer);
    }

    public void openactivityuser() {
        Intent intentUser;
        //creo la nuova pagina (intentUser)
        intentUser = new Intent(this, UserPage.class);
        //se non mi interessa ricevere delle informazioni dalla pagina figlia allora posso usare
        //direttamente STARTACTIVITY
        startActivity(intentUser);

    }
    public void openactivityDiary(){
        Intent intentDiary = new Intent(this, MainActivity.class);
        startActivity(intentDiary);
    }

}