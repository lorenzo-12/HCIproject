package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class UpdateFoodPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int SELECT_IMAGE = 1;

    DBHelper db;
    EditText update_name, update_carb, update_prot, update_fat,update_kal;
    Spinner update_spinner_category;
    Button updateFoodbtn;
    String update_category_string;
    Boolean changes = false;
    ImageView image;
    Bitmap image_bitmap;

    String original_name,original_category,original_carb,original_prot,original_fat,original_kal;

    @Override
    public void onBackPressed() {
        changes = false;
        Intent i = new Intent();
        Log.d("UpdateFoodPage","onBackPressed");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 0);
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
        update_kal = findViewById(R.id.food_kal_text_update);
        updateFoodbtn = findViewById(R.id.updateFoodbtn);
        image = findViewById(R.id.foodimageview_update);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateFoodPage.this,R.array.food_category_possible, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        update_spinner_category.setAdapter(adapter);

        update_spinner_category.setOnItemSelectedListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Update food Picture"), SELECT_IMAGE);
            }
        });

        updateFoodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = updateFood();
                //Toast.makeText(UpdateFoodPage.this,result.toString(),Toast.LENGTH_SHORT).show();
                changes = true;
                passData();
                onBackPressed();
            }
        });

        loadData();
        image.setImageBitmap(db.loadImage(original_name));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageuri = data.getData();
            try {
                image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                image.setImageBitmap(image_bitmap);
            } catch (IOException e) {
                image.setImageDrawable(getDrawable(R.drawable.no_image2));
            }
        }
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        original_name = sharedPreferences.getString("food_name","");
        original_category = sharedPreferences.getString("food_category","other");
        original_carb = sharedPreferences.getString("food_carb","0");
        original_prot = sharedPreferences.getString("food_prot","0");
        original_fat = sharedPreferences.getString("food_fat","0");
        original_kal = sharedPreferences.getString("food_kal","0");
        changes = sharedPreferences.getBoolean("food_changes",false);

        if (update_name != null) update_name.setText(original_name);
        if (update_carb != null) update_carb.setText(original_carb);
        if (update_prot != null) update_prot.setText(original_prot);
        if (update_fat != null) update_fat.setText(original_fat);
        if (update_kal != null) update_kal.setText(original_kal);
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

    public void passData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("food_changes",changes);
        editor.apply();
    }

    public Boolean updateFood(){
        String old_name = original_name.toLowerCase();
        String new_name = update_name.getText().toString().toLowerCase();
        String category = update_category_string;
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap img = drawable.getBitmap();
        int carb,prot,fat,kal;
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
        try {
            kal = Integer.parseInt(update_kal.getText().toString());
            if (kal<0) fat = -kal;
        } catch (Exception e1){
            kal = 0;
        }
        Boolean check_modification = db.modifyFood(old_name,new_name,category,img,carb,prot,fat,kal);
        if (!check_modification) return false;
        else return true;
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