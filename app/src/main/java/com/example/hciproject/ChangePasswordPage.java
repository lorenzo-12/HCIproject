package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordPage extends AppCompatActivity {

    public static final String LIGHTMODE = "lightmode";
    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";
    public static final String USER_LOGGED = "user_logged";
    public Boolean lightmode;
    public String user_logged;

    EditText curpassword,newpassword;
    Button logout,reset;
    TextView debug;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //se l'utente preme "<-" e non si modifca questa funzione quello che accade è che l'Activity non restituisce in outout
    //il database modificato, pertanto è necessario fare si che quando si preme tale bottone si invii anche il db modificato

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        saveData();
        Intent i = new Intent();
        Log.d("UserPage","onBackPressed");
        //codice necessario per far si che ci sia un minimo di delay nel passggio dalla pagina di login alla main page
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //finish server poichè senza di essa non viene effettivamente mandato indietro il messaggio con il database
                //finish inoltre stoppa la pagina corrente e torna a quella precedente
                finish();
            }
        }, 500);
         */
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_page);

        reset = findViewById(R.id.resetbtn);
        curpassword = findViewById(R.id.curpasswordtxt);
        newpassword = findViewById(R.id.newpasswordtxt);
        debug = findViewById(R.id.debug);
        db = new DBHelper(this);
        curpassword.addTextChangedListener(resetTextWatcher);
        newpassword.addTextChangedListener(resetTextWatcher);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curpsw = curpassword.getText().toString().toLowerCase();
                String newpsw = newpassword.getText().toString().toLowerCase();
                if (!curpsw.isEmpty() && !newpsw.isEmpty()){
                    Boolean check = db.checkUser(user_logged,curpsw);
                    if (check){
                        Boolean res = db.updateUserPassword(user_logged,newpsw);
                        if (res) {
                            Toast.makeText(ChangePasswordPage.this,"Password updated correctly",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChangePasswordPage.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ChangePasswordPage.this,"Wrong current password",Toast.LENGTH_SHORT).show();
                    }
                }
                saveData();
                onBackPressed();
            }
        });

        loadData();
        updateButtons();
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
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        lightmode = sharedPreferences.getBoolean(LIGHTMODE,true);
        user_logged = sharedPreferences.getString(USER_LOGGED, "UL:none\n");
    }

    public void updateButtons(){
        String curpsw = "";
        String newpsw = "";
        if ((curpassword != null) && (newpassword != null)) {
            curpsw = curpassword.getText().toString();
            newpsw = newpassword.getText().toString();
        }
        if (!curpsw.isEmpty() && !newpsw.isEmpty()){
            reset.setEnabled(true);
        } else {
            reset.setEnabled(false);
        }
    }
}