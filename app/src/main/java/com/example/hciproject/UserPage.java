package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.IOException;

public class UserPage extends AppCompatActivity {

    public static final String WEIGHT_S = "SIGNUP_WEIGHT";
    public static final String HEIGHT_S = "SINGUP_HEIGHT";
    public static final String CARB_S = "SINGUP_CARB";
    public static final String PROT_S = "SINGUP_PROT";
    public static final String FAT_S = "SINGUP_FAT";
    public static final String KAL_S = "SINGUP_KAL";
    public static final String SEX_S = "SIGNUP_SEX";

    public static final String LIGHTMODE = "lightmode";
    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";
    public static final String USER_LOGGED = "user_logged";
    public static final int SELECT_IMAGE = 1;
    public static final int CHANGE_IMAGE = 2;
    public String user_logged;
    public Boolean lightmode;
    public Menu menu_bar;
    public Boolean image_selected = false;

    Integer sex = 0; //0: not selected, 1: male, 2: female
    Integer weight_value,height_value,carb_value,prot_value,fat_value, cal_value;

    ConstraintLayout layout;
    EditText old_pasword, new_password,carb,prot,fat,cal;
    Button change_image,change_password,change_goals;
    ImageView image,logout;
    DBHelper db;
    Uri imageuri;
    Bitmap image_bitmap;
    BottomNavigationView nav;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*
         * without call to super onBackPress() will not be called when
         * keyCode == KeyEvent.KEYCODE_BACK
         */
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.d("MainActivity","onBackPressed");
        saveData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }

    /*

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        loadData();
        getMenuInflater().inflate(R.menu.menu, menu);
        menu_bar = menu;

        MenuItem item_logout = menu.findItem(R.id.Logout_item);
        MenuItem item_psw_change = menu.findItem(R.id.Change_password_item);
        MenuItem item_goals_change = menu.findItem(R.id.Change_username_item);
        updateButtons();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadData();
        MenuItem item_logout = menu_bar.findItem(R.id.Logout_item);
        MenuItem item_psw_change = menu_bar.findItem(R.id.Change_password_item);
        MenuItem item_goals_change = menu_bar.findItem(R.id.Change_username_item);

        //azioni da eseguire quando si preme uno dei possibili item del menu a tendina
        switch (item.getItemId()){
            case R.id.Logout_item:
                user_logged = "none";
                saveData();
                updateButtons();
                openactivityFitlife();
                return true;
            case R.id.Change_password_item:
                Intent intentpassword = new Intent(this, ChangePasswordPage.class);
                startActivity(intentpassword);
                return true;
            case R.id.Change_username_item:
                Intent intentGoals = new Intent(this, ChangeGoalsPage.class);
                startActivity(intentGoals);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        layout = findViewById(R.id.constraintLayout);
        old_pasword = findViewById(R.id.old_password_EditText);
        new_password = findViewById(R.id.new_password_EditText);
        carb = findViewById(R.id.carb_EditText);
        prot = findViewById(R.id.prot_EditText);
        fat = findViewById(R.id.fat_EditText);
        cal = findViewById(R.id.cal_EditText);
        logout = findViewById(R.id.user_logout_btn);
        change_password = findViewById(R.id.change_password_btn);
        change_goals = findViewById(R.id.change_goals_btn);
        db = new DBHelper(this);

        old_pasword.addTextChangedListener(resetTextWatcher);
        new_password.addTextChangedListener(resetTextWatcher);
        carb.addTextChangedListener(resetTextWatcher);
        prot.addTextChangedListener(resetTextWatcher);
        fat.addTextChangedListener(resetTextWatcher);
        cal.addTextChangedListener(resetTextWatcher);

        image = findViewById(R.id.userimageview);
        change_image = findViewById(R.id.change_image_btn);

        nav = findViewById(R.id.bottomnavigatorviewUser);

        //così quando apro l'app mi da fin  da subito selezionata l'icona del cibo
        nav.setSelectedItemId(R.id.bottom_user);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_diary:
                        openactivityDiary();
                        return true;
                    case R.id.bottom_exercise:
                        Toast.makeText(UserPage.this,"exercise",Toast.LENGTH_SHORT).show();
                        openactivityexercise();
                        return true;
                    case R.id.bottom_food:
                        Toast.makeText(UserPage.this,"food",Toast.LENGTH_SHORT).show();
                        openactivityFood();
                        return true;
                    case R.id.bottom_time:
                        Toast.makeText(UserPage.this,"timer",Toast.LENGTH_SHORT).show();
                        openactivitytimer();
                        return true;
                    case R.id.bottom_user:
                        //do nothing, im alredy in user activity
                        return true;
                }
                return true;
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_user_password();
            }
        });

        change_goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_user_goals();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_user();
            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (change_image.getText().toString().equals("select image")){
                    change_image.setText("change image");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                }
                else if (change_image.getText().toString().equals("change image")){
                    startActivityForResult(Intent.createChooser(intent,"Change Picture"), CHANGE_IMAGE);
                }

            }
        });


        loadData();
        updateButtons();

        //String res = db.viewUsers();
        //username.setText(res);

        change_image.setText("select image");
        if (!user_logged.equals("none")){
            Bitmap b = loadImage(user_logged.toLowerCase());
            image.setImageBitmap(b);
            change_image.setText("change image");
        }

        fill_values();
    }

    public Bitmap loadImage(String name){
        name = name + ".jpg";
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try{
            fileInputStream = this.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==SELECT_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri = data.getData();
            try {
                image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                image.setImageBitmap(image_bitmap);
                if (user_logged.equals("none")) change_image.setEnabled(false);
                image_selected=true;
            } catch (IOException e) {
                image.setImageDrawable(getDrawable(R.drawable.no_image2));
            }
        }

        else if (requestCode==CHANGE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri = data.getData();
            try {
                image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                image.setImageBitmap(image_bitmap);
                db.deleteImage(user_logged);
                db.saveImage(image_bitmap,user_logged);
            } catch (IOException e) {
                image.setImageDrawable(getDrawable(R.drawable.no_image2));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
        updateButtons();
        String res = db.viewUsers();
        if (!user_logged.equals("none")) image.setImageBitmap(db.loadImage(user_logged));
        else if (user_logged.equals("none") && image_selected==false) image.setImageResource(R.drawable.no_image2);
    }

    public TextWatcher resetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateButtons();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGGED,user_logged);
        editor.putBoolean(LIGHTMODE,lightmode);
        editor.putInt(CARB_S,carb_value);
        editor.putInt(PROT_S,prot_value);
        editor.putInt(FAT_S,fat_value);
        editor.putInt(KAL_S,cal_value);
        editor.apply();
        updateButtons();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        lightmode = sharedPreferences.getBoolean(LIGHTMODE,true);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
        sex = sharedPreferences.getInt(SEX_S,0);
        weight_value = sharedPreferences.getInt(WEIGHT_S,0);
        height_value = sharedPreferences.getInt(HEIGHT_S,0);
        carb_value = sharedPreferences.getInt(CARB_S,0);
        prot_value = sharedPreferences.getInt(PROT_S,0);
        fat_value = sharedPreferences.getInt(FAT_S,0);
        cal_value = sharedPreferences.getInt(KAL_S,0);

        updateButtons();
    }

    public void updateButtons(){
        //controllo se l'utente è loggato poichè se non è loggato allora non può fare queste cose


        MenuItem item_logout = null;
        MenuItem item_usr_change = null;
        MenuItem item_psw_change = null;
        if (menu_bar != null) {
            item_logout = menu_bar.findItem(R.id.Logout_item);
            item_psw_change = menu_bar.findItem(R.id.Change_password_item);
            item_usr_change = menu_bar.findItem(R.id.Change_username_item);
        }
        String old_p = "";
        String new_p = "";
        if (old_pasword != null) {
            old_p = old_pasword.getText().toString().toLowerCase();
        }
        if (new_password != null) {
            new_p = new_password.getText().toString().toLowerCase();
        }
        if ((menu_bar != null) && (user_logged.equals("none"))){
            //Toast.makeText(UserPage.this,"if "+user_logged,Toast.LENGTH_SHORT).show();
            item_logout.setEnabled(false);
            item_psw_change.setEnabled(false);
            item_usr_change.setEnabled(false);
        }
        else if (menu_bar != null) {
            //Toast.makeText(UserPage.this,"else if "+user_logged,Toast.LENGTH_SHORT).show();
            item_logout.setEnabled(true);
            item_psw_change.setEnabled(true);
            item_usr_change.setEnabled(true);
        }
        if (!old_p.equals("") && !new_p.equals("")) change_password.setClickable(true);
        else change_password.setClickable(false);
        if (!user_logged.equals("none")) change_image.setEnabled(true);

        String c,p,f,k;
        c = carb.getText().toString();
        p = prot.getText().toString();
        f = fat.getText().toString();
        k = cal.getText().toString();
        if (!c.equals("") && !f.equals("") && !p.equals("") && !k.equals("")) change_goals.setClickable(true);
        else change_goals.setClickable(false);

    }

    public void fill_values(){
        Cursor cursor = db.getUserInfo(user_logged);
        cursor.moveToNext();
        old_pasword.setText(cursor.getString(1));
        carb.setText(String.valueOf(cursor.getInt(5)));
        prot.setText(String.valueOf(cursor.getInt(6)));
        fat.setText(String.valueOf(cursor.getInt(7)));
        cal.setText(String.valueOf(cursor.getInt(8)));
        carb_value = cursor.getInt(5);
        prot_value = cursor.getInt(6);
        fat_value = cursor.getInt(7);
        cal_value = cursor.getInt(8);
    }

    public void change_user_password(){
        Boolean check = db.findUser(user_logged);
        if (check){
            Cursor cursor = db.getUserInfo(user_logged);
            if (cursor != null){
                cursor.moveToNext();
                if (cursor.getString(1).equals(old_pasword.getText().toString().toLowerCase())){
                    db.updateUserPassword(user_logged,new_password.getText().toString().toLowerCase());
                }
                else Toast.makeText(UserPage.this,"wrong password",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(UserPage.this,"no info", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(UserPage.this,"user not exist",Toast.LENGTH_SHORT).show();
    }

    public void logout_user(){
        user_logged = "none";
        saveData();
        openactivityFitlife();
    }

    public void change_user_goals(){
        Boolean check = db.updateUserGoals(user_logged,Integer.parseInt(carb.getText().toString()),Integer.parseInt(prot.getText().toString()),Integer.parseInt(fat.getText().toString()),Integer.parseInt(cal.getText().toString()));
        Toast.makeText(UserPage.this, check.toString(), Toast.LENGTH_SHORT).show();
        saveData();
        loadData();
        fill_values();
    }

    public void openactivityDiary(){
        saveData();
        Intent intentDiary = new Intent(this, MainActivity.class);
        startActivity(intentDiary);
    }

    public void openactivityFood(){
        saveData();
        Intent intentDiet = new Intent(this, FoodPage.class);
        startActivity(intentDiet);
    }

    public void openactivityexercise(){
        saveData();
        Intent intentExercise = new Intent(this, ExercisePage.class);
        startActivity(intentExercise);
    }

    public void openactivitytimer(){
        saveData();
        Intent intentTimer = new Intent(this, TimerPage.class);
        startActivity(intentTimer);
    }

    public void openactivityFitlife(){
        saveData();
        Intent intentFitlife = new Intent(this, FitlifePage.class);
        startActivity(intentFitlife);
    }

}