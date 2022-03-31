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

public class AddFoodPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";

    public static final String CNAME_FOOD = "food_name";
    public static final String CCATEGORY_FOOD = "food_category";
    public static final String CCARB = "carb";
    public static final String CPROT = "prot";
    public static final String CFAT = "fat";

    DBHelper db;
    EditText input_name,input_carb,input_prot,input_fat;
    Spinner input_category_spinner;
    Button addFoodbtn;
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
        setContentView(R.layout.activity_addfoodpage);

        db = new DBHelper(this);
        input_name = findViewById(R.id.food_name_text);
        input_category_spinner = findViewById(R.id.food_category_spinner);
        input_carb = findViewById(R.id.food_carb_text);
        input_prot = findViewById(R.id.food_prot_text);
        input_fat = findViewById(R.id.food_fat_text);
        addFoodbtn = findViewById(R.id.addFoodbtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddFoodPage.this,R.array.food_category_possible, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_category_spinner.setAdapter(adapter);

        input_category_spinner.setOnItemSelectedListener(this);

        addFoodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = addFood();
                Toast.makeText(AddFoodPage.this,result.toString(),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }

    public Boolean addFood(){
        String name = input_name.getText().toString().toLowerCase();
        addFoodbtn.setEnabled(false);

        String error = "";
        if (name.isEmpty()) {
            Toast.makeText(AddFoodPage.this,"Please insert a name for the food",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (input_category.isEmpty()){
            Toast.makeText(AddFoodPage.this,"Please insert a category for the food",Toast.LENGTH_SHORT).show();
            return false;
        }
        addFoodbtn.setEnabled(true);

        int carb,prot,fat;
        try {
            carb = Integer.parseInt(input_carb.getText().toString());
            if (carb<0) carb = -carb;
        } catch (Exception e1){
            carb = 0;
            error += "Carbohydrates field not a number\n";
        }
        try {
            prot = Integer.parseInt(input_prot.getText().toString());
            if (prot<0) prot = -prot;
        } catch (Exception e2){
            prot = 0;
            error += "Protein field not a number\n";
        }
        try {
            fat = Integer.parseInt(input_fat.getText().toString());
            if (fat<0) fat = -fat;
        } catch (Exception e3){
            fat = 0;
            error += "Fat field not a number\n";
        }

        Boolean check = db.findFood(name);
        Boolean result = false;
        if (check){
            result = db.updateFood(name,input_category,carb,prot,fat);
            return result;
        } else {
            result = db.addFood(name,input_category,carb,prot,fat);
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