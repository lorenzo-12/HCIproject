package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class UserPage extends AppCompatActivity {

    public static final String LIGHTMODE = "lightmode";
    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";
    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    public Boolean lightmode;
    public Menu menu_bar;


    ConstraintLayout layout;
    EditText username,password;
    Button login,signup;
    TextView debug,debug2;
    DBHelper db;


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
                debug.setText(user_logged);
                return true;
            case R.id.Change_password_item:
                Intent intentpassword = new Intent(this, ChangePasswordPage.class);
                startActivity(intentpassword);
                return true;
            case R.id.Change_username_item:
                //Intent intentUsername = new Intent(this, ChangeUsernamePage.class);
                //startActivity(intentUsername);
                return true;
            case R.id.LightMode_item:
                /*
                if (lightmode == true){
                    Toast.makeText(this,"Pass to DarkMode",Toast.LENGTH_SHORT).show();
                    lightmode = false;
                    menu_bar.getItem(3).setIcon(R.drawable.ic_baseline_wb_sunny_24);
                    layout.setBackgroundColor(Color.BLACK);
                    saveData();
                }
                else {
                    Toast.makeText(this,"Pass to LightMode",Toast.LENGTH_SHORT).show();
                    lightmode = true;
                    menu_bar.getItem(3).setIcon(R.drawable.ic_baseline_dark_mode_24);
                    layout.setBackgroundColor(Color.GRAY);
                    saveData();
                }
                 */
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

        debug = findViewById(R.id.debugtxt);
        debug2 = findViewById(R.id.debugtxt2);

        username.addTextChangedListener(resetTextWatcher);
        password.addTextChangedListener(resetTextWatcher);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usr = username.getText().toString().toLowerCase();
                String psw = password.getText().toString().toLowerCase();

                if (usr.isEmpty() || psw.isEmpty()) {
                    Toast.makeText(UserPage.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean check = db.addUser(usr,psw);
                if (check){
                    Toast.makeText(UserPage.this,"OK",Toast.LENGTH_SHORT).show();
                    user_logged = usr;

                }else {
                    Toast.makeText(UserPage.this,"FAIL",Toast.LENGTH_SHORT).show();
                }
                String res = db.viewUsers();
                saveData();
                updateButtons();
                debug.setText(user_logged);
                debug2.setText(res);
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
                debug.setText(user_logged);
                debug2.setText(res);
            }
        });
        loadData();
        updateButtons();
        debug.setText(user_logged);
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*
        layout = findViewById(R.id.constraintLayout);
        if (lightmode == true) {
            layout.setBackgroundColor(Color.BLACK);
            lightmode = false;
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            lightmode = true;
        }
         */
        loadData();
        updateButtons();
        String res = db.viewUsers();
        debug2.setText(res);
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

    }




}