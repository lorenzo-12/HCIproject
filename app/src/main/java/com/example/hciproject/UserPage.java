package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

    ConstraintLayout layout;
    EditText username,password;
    Button login,signup,select;
    ImageView image;
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
        MenuItem item_usr_change = menu.findItem(R.id.Change_username_item);
        updateButtons();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadData();
        MenuItem item_logout = menu_bar.findItem(R.id.Logout_item);
        MenuItem item_psw_change = menu_bar.findItem(R.id.Change_password_item);
        MenuItem item_usr_change = menu_bar.findItem(R.id.Change_username_item);

        //azioni da eseguire quando si preme uno dei possibili item del menu a tendina
        switch (item.getItemId()){
            case R.id.Logout_item:
                user_logged = "none";
                saveData();
                updateButtons();
                return true;
            case R.id.Change_password_item:
                Intent intentpassword = new Intent(this, ChangePasswordPage.class);
                startActivity(intentpassword);
                return true;
            case R.id.Change_username_item:
                //Intent intentUsername = new Intent(this, ChangeUsernamePage.class);
                //startActivity(intentUsername);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        layout = findViewById(R.id.constraintLayout);
        username = (EditText) findViewById(R.id.usernametxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        login = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signupbtn);
        db = new DBHelper(this);

        username.addTextChangedListener(resetTextWatcher);
        password.addTextChangedListener(resetTextWatcher);

        image = findViewById(R.id.userimageview);
        select = findViewById(R.id.selectimagebtn);

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

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (select.getText().toString().equals("select image")){
                    select.setText("change image");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                }
                else if (select.getText().toString().equals("change image")){
                    startActivityForResult(Intent.createChooser(intent,"Change Picture"), CHANGE_IMAGE);
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usr = username.getText().toString().toLowerCase();
                String psw = password.getText().toString().toLowerCase();

                if (usr.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(UserPage.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean check;
                try {
                    BitmapDrawable bd = (BitmapDrawable) image.getDrawable();
                    image_bitmap = bd.getBitmap();
                    check = db.addUser(usr,psw,image_bitmap);
                } catch (Exception e){
                    check = db.addUser(usr,psw);
                }

                if (check){
                    Toast.makeText(UserPage.this,"OK",Toast.LENGTH_SHORT).show();
                    user_logged = usr;

                }else {
                    Toast.makeText(UserPage.this,"FAIL",Toast.LENGTH_SHORT).show();
                }
                String res = db.viewUsers();
                saveData();
                updateButtons();
                onBackPressed();
            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString().toLowerCase();
                String psw = password.getText().toString().toLowerCase();

                if (usr.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(UserPage.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                Boolean check = db.checkUser(usr,psw);
                if (check){
                    Toast.makeText(UserPage.this,"OK",Toast.LENGTH_SHORT).show();
                    user_logged = usr;
                }else {
                    Toast.makeText(UserPage.this,"FAIL",Toast.LENGTH_SHORT).show();
                }
                String res = db.viewUsers();
                saveData();
                updateButtons();
                onBackPressed();
            }
        });

        loadData();
        updateButtons();

        select.setText("select image");
        if (!user_logged.equals("none")){
            Bitmap b = loadImage(user_logged.toLowerCase());
            image.setImageBitmap(b);
            select.setText("change image");
        }
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
                if (user_logged.equals("none")) select.setEnabled(false);
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
        editor.apply();
        updateButtons();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        lightmode = sharedPreferences.getBoolean(LIGHTMODE,true);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
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
        String usr = "";
        String psw = "";
        if (username != null) {
            usr = username.getText().toString().toLowerCase();
        }
        if (password != null) {
            psw = password.getText().toString().toLowerCase();
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
        if (!usr.isEmpty() && !psw.isEmpty() && (user_logged.equals("none"))) {
            signup.setEnabled(true);
            login.setEnabled(true);
        }
        else{
            login.setEnabled(false);
            signup.setEnabled(false);
        }

        if (!user_logged.equals("none")) select.setEnabled(true);

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


}