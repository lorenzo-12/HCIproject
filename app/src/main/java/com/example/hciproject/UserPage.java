package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Locale;

public class UserPage extends AppCompatActivity {


    //variabili globali usate dalla pagina User per il login
    ConstraintLayout layout;
    EditText username,password;
    Button login,signup;
    TextView debug,debug2;
    DB db;
    Boolean lightmode;


    //variabili necessarie per salvare lo stato dei pulsanti,testo ecc...
    //poichè se un pulsante lo setto a NON CLICCABILE ma poi cambio pagina tale cambiamento
    //non rimane salvato, quindi lo devo fare manualmente
    public static final String DATABASE = "database";
    public String db_s;
    public static final String LIGHTMODE = "lightmode";
    public Boolean lightmode_s;
    public Menu menu_bar;


    //parte necessaria affinchè quando l'utente preme "<-" per tornare alla pagina precedente la funzione
    //onBackPressed venga chiamata

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*
         * without call to super onBackPress() will not be called when
         * keyCode == KeyEvent.KEYCODE_BACK
         */
        return super.onKeyUp(keyCode, event);
    }

    //se l'utente preme "<-" e non si modifca questa funzione quello che accade è che l'Activity non restituisce in outout
    //il database modificato, pertanto è necessario fare si che quando si preme tale bottone si invii anche il db modificato

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent();
        Log.d("MainActivity","onBackPressed");
        //codice necessario per far si che ci sia un minimo di delay nel passggio dalla pagina di login alla main page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //finish server poichè senza di essa non viene effettivamente mandato indietro il messaggio con il database
                //finish inoltre stoppa la pagina corrente e torna a quella precedente
                finish();
            }
        }, 500);
        //finish();
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    //quando crea il menu a tendina chiama questa funzione
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //mi carico le informazioni
        loadData();
        updateView();
        getMenuInflater().inflate(R.menu.menu, menu);
        menu_bar = menu;

        //controllo se l'utente è loggato poichè se non è loggato allora non può fare queste cose
        MenuItem item_logout = menu.findItem(R.id.Logout_item);
        MenuItem item_psw_change = menu.findItem(R.id.Change_password_item);
        MenuItem item_usr_change = menu.findItem(R.id.Change_username_item);
        if (db.User_logged.equals("none")){
            item_logout.setEnabled(false);
            item_psw_change.setEnabled(false);
            item_usr_change.setEnabled(false);
        }
        else {
            item_logout.setEnabled(true);
            item_psw_change.setEnabled(true);
            item_usr_change.setEnabled(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadData();
        updateView();

        MenuItem item_logout = menu_bar.findItem(R.id.Logout_item);
        MenuItem item_psw_change = menu_bar.findItem(R.id.Change_password_item);
        MenuItem item_usr_change = menu_bar.findItem(R.id.Change_username_item);

        //azioni da eseguire quando si preme uno dei possibili item del menu a tendina
        switch (item.getItemId()){
            case R.id.Logout_item:
                db.unsetUser();
                Toast.makeText(this,"Logout successful",Toast.LENGTH_LONG).show();
                item_logout.setEnabled(false);
                item_psw_change.setEnabled(false);
                item_usr_change.setEnabled(false);
                saveData();
                return true;
            case R.id.Change_password_item:
                Intent intentpassword = new Intent(this, ChangePasswordPage.class);
                startActivity(intentpassword);
                return true;
            case R.id.Change_username_item:
                Intent intentUsername = new Intent(this, ChangeUsernamePage.class);
                startActivity(intentUsername);
                return true;
            case R.id.LightMode_item:
                //se stavo in light mode allora devo passare in dark mode
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

        //variabili per il debug
        debug = findViewById(R.id.debugtxt);
        debug2 = findViewById(R.id.debugtxt2);

        username.addTextChangedListener(resetTextWatcher);
        password.addTextChangedListener(resetTextWatcher);


        //funzione che si attiva quando clicchiamo sul pulsante di  SIGN UP
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo username e password inseriti dall'utente
                String usr = username.getText().toString().toLowerCase(Locale.ROOT);
                String psw = password.getText().toString().toLowerCase(Locale.ROOT);
                User u = new User(usr,psw);

                //se uno dei due è vuoto mostro un messaggio in cui riporto l'errore
                if (usr.equals("") || psw.equals("")){
                    if (usr.equals("")){
                        Toast.makeText(UserPage.this,"please enter the Username",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(UserPage.this,"please enter the Password",Toast.LENGTH_LONG).show();
                    }
                }
                //se invece entrambi i campi sono compilati
                else {
                    //siccome l'utente si sta iscrivendo controllo che lo Username inserito non esista già
                    boolean different_username = true;
                    for (User e : db.users_list) {
                        if (e.username.equals(usr)) {
                            different_username = false;
                        }
                    }

                    //se lo Username inserito non esiste già
                    if (different_username == true) {
                        //inserisco l'utente all'interno del database
                        db.addUser(u);
                        db.setUser(u);
                        //mostro un messaggio in cui notifico all'utente che la procedura è andata a buon fine
                        Toast.makeText(UserPage.this,"SignUp successful",Toast.LENGTH_LONG).show();
                        debug.setText(usr+" "+psw+" "+db.User_logged);
                        onBackPressed();
                    }

                    //se invece lo Username inserito esiste già allora notifico l'utente di ciò
                    else if (different_username == false){
                        Toast.makeText(UserPage.this,"Username already used, please try another username",Toast.LENGTH_LONG).show();
                    }
                }

                //siccome nel passare da una pagina all'altra le informazioni relative ai pulsanti,testo ecc...
                //non vengono salvate quello che faccio è salvare tali informazioni in una struttura chiamata SHARED_PREFERENCE

                saveData();
                //onBackPressed();
            }
        });

        //funzione chiamata quando l'utente si vuole loggare
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ottengo lo Username e password inseriti dall'utente
                String usr = username.getText().toString();
                String psw = password.getText().toString();

                //controllo che username e password siano veramente quelli inseriti dall'utente
                debug.setText(usr+" "+psw);
                User u = new User(usr,psw);

                //controllo se uno dei due campi (o entrambi sono vuoti)
                if (usr.equals("") || psw.equals("")){
                    if (usr.equals("")){
                        Toast.makeText(UserPage.this,"please enter the Username",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(UserPage.this,"please enter the Password",Toast.LENGTH_LONG).show();
                    }
                }

                //controllo se l'utente esiste già nel database
                else if (db.users_list.contains(u)){
                    //anche se non sto facendo cambiamenti al database comunque la pagina PADRE si aspetta un risultato
                    //quindi semplicemente gli rimando indietro il database che mi ha mandato lui
                    db.setUser(u);

                    //avviso l'utente che il login è andato a buon fine
                    Toast.makeText(UserPage.this,"login successful",Toast.LENGTH_LONG).show();
                    debug.setText(usr+" "+psw+db.User_logged);
                }

                //se l'utente inserito non è nel database avviso l'utente che username e/o password sono sbagliati
                else {
                    Toast.makeText(UserPage.this,"Wrong Username or Password",Toast.LENGTH_LONG).show();
                }

                //siccome nel passare da una pagina all'altra le informazioni relative ai pulsanti,testo ecc...
                //non vengono salvate quello che faccio è salvare tali informazioni in una struttura chiamata SHARED_PREFERENCE

                invalidateOptionsMenu();
                saveData();
                onBackPressed();
            }
        });

        //funzioni che riprendono e applicano lo stato precedente dei bottoni,testi ecc...
        //VANNO MESSI SEMPRE ALLA FINE SENNO L'APP CRASHA
        loadData();
        updateView();


        if (db.User_logged.equals("none") == false){
            User tmp = db.getUser(db.User_logged);
            if (tmp != null) {
                username.setText(tmp.username);
                password.setText(tmp.password);
            }
        }
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        updateView();
        layout = findViewById(R.id.constraintLayout);
        if (lightmode == true) {
            layout.setBackgroundColor(Color.BLACK);
            lightmode = false;
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            lightmode = true;
        }
        saveData();
    }

     */

    public TextWatcher resetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String usr = username.getText().toString();
            String psw = password.getText().toString();

            debug.setText(usr+" "+psw);

            login.setEnabled(false);
            signup.setEnabled(false);
            if (!usr.isEmpty() && !psw.isEmpty()) {
                signup.setEnabled(true);
                login.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    //funzioni che salvano, caricano e applicano lo stato dei pulsanti,testo ecc...


    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(DATABASE,db.toString());
        editor.putBoolean(LIGHTMODE,lightmode);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);

        lightmode_s = sharedPreferences.getBoolean(LIGHTMODE,true);
        db_s = sharedPreferences.getString(DATABASE, "UL:none\n");
    }

    public void updateView(){
        db = new DB(db_s);
        lightmode = lightmode_s;
        debug2.setText(db_s);
    }


}