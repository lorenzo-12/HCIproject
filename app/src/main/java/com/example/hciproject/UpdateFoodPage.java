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

public class UpdateFoodPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String USER_TABLE = "users";
    public static final String CNAME_FOOD = "food_name";

    DBHelper db;
    EditText update_name, update_carb, update_prot, update_fat;
    Spinner update_spinner_category;
    Button updateFoodbtn;
    String update_category_string;

    String original_name,original_category,original_carb,original_prot,original_fat;

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
        setContentView(R.layout.activity_updatefoodpage);

        db = new DBHelper(this);
        update_name = findViewById(R.id.food_name_text_update);
        update_spinner_category = findViewById(R.id.food_category_spinner_update);
        update_carb = findViewById(R.id.food_carb_text_update);
        update_prot = findViewById(R.id.food_prot_text_update);
        update_fat = findViewById(R.id.food_fat_text_update);
        updateFoodbtn = findViewById(R.id.updateFoodbtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateFoodPage.this,R.array.food_category_possible, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_spinner_category.setAdapter(adapter);

        update_spinner_category.setOnItemSelectedListener(this);

        updateFoodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = updateFood();
                //Toast.makeText(UpdateFoodPage.this,result.toString(),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        loadData();

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        original_name = sharedPreferences.getString("food_name","");
        original_category = sharedPreferences.getString("food_category","other");
        original_carb = sharedPreferences.getString("food_carb","0");
        original_prot = sharedPreferences.getString("food_prot","0");
        original_fat = sharedPreferences.getString("food_fat","0");

        if (update_name != null) update_name.setText(original_name);
        if (update_carb != null) update_carb.setText(original_carb);
        if (update_prot != null) update_prot.setText(original_prot);
        if (update_fat != null) update_fat.setText(original_fat);
        if (update_category_string != null) {
            update_category_string = original_category;
            if (update_category_string.equals("fruitvegetable")) update_spinner_category.setSelection(1);
            else if (update_category_string.equals("cereal")) update_spinner_category.setSelection(2);
            else if (update_category_string.equals("dairy")) update_spinner_category.setSelection(3);
            else if (update_category_string.equals("meatfisheggs")) update_spinner_category.setSelection(4);
            else if (update_category_string.equals("sweet")) update_spinner_category.setSelection(5);
            else update_spinner_category.setSelection(6);
        }

    }

    public Boolean updateFood(){
        String old_name = original_name;
        String new_name = update_name.getText().toString().toLowerCase();
        String category = update_category_string;
        int carb,prot,fat;
        try {
            carb = Integer.parseInt(update_carb.getText().toString());
            if (carb<0) carb = -carb;
        } catch (Exception e1){
            carb = 0;
        }
        try {
            prot = Integer.parseInt(update_prot.getText().toString());
            if (prot<0) prot = -prot;
        } catch (Exception e1){
            prot = 0;
        }
        try {
            fat = Integer.parseInt(update_fat.getText().toString());
            if (fat<0) fat = -fat;
        } catch (Exception e1){
            fat = 0;
        }
        Boolean check_delete = db.deleteFood(old_name);
        if (!check_delete) return false;
        Boolean check_insert = db.addFood(new_name,category,carb,prot,fat);
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