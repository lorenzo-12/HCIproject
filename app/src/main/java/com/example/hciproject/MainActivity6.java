package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity6 extends AppCompatActivity {


    //variabili globali usate dalla pagina User per il login
    ConstraintLayout layout;
    EditText username,password;
    Button login,signup;
    TextView debug,debug2;
    DB db;

    //variabili necessarie per salvare lo stato dei pulsanti,testo ecc...
    //poichè se un pulsante lo setto a NON CLICCABILE ma poi cambio pagina tale cambiamento
    //non rimane salvato, quindi lo devo fare manualmente
    public static final String DATABASE = "database";
    public String db_s;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        layout = findViewById(R.id.constraintLayout);
        username = (EditText) findViewById(R.id.usernametxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        login = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signupbtn);

        //variabili per il debug
        debug = findViewById(R.id.debugtxt);
        debug2 = findViewById(R.id.debugtxt2);


        //funzione che si attiva quando clicchiamo sul pulsante di  SIGN UP
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo username e password inseriti dall'utente
                String usr = username.getText().toString();
                String psw = password.getText().toString();
                User u = new User(usr,psw);

                //se uno dei due è vuoto mostro un messaggio in cui riporto l'errore
                if (usr.equals("") || psw.equals("")){
                    if (usr.equals("")){
                        Toast.makeText(MainActivity6.this,"please enter the Username",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity6.this,"please enter the Password",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity6.this,"SignUp successful",Toast.LENGTH_LONG).show();
                        debug.setText(usr+" "+psw+" "+db.User_logged);
                    }

                    //se invece lo Username inserito esiste già allora notifico l'utente di ciò
                    else if (different_username == false){
                        Toast.makeText(MainActivity6.this,"Username already used, please try another username",Toast.LENGTH_LONG).show();
                    }
                }

                //siccome nel passare da una pagina all'altra le informazioni relative ai pulsanti,testo ecc...
                //non vengono salvate quello che faccio è salvare tali informazioni in una struttura chiamata SHARED_PREFERENCE

                saveData();
                onBackPressed();
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
                        Toast.makeText(MainActivity6.this,"please enter the Username",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity6.this,"please enter the Password",Toast.LENGTH_LONG).show();
                    }
                }

                //controllo se l'utente esiste già nel database
                else if (db.users_list.contains(u)){
                    //anche se non sto facendo cambiamenti al database comunque la pagina PADRE si aspetta un risultato
                    //quindi semplicemente gli rimando indietro il database che mi ha mandato lui
                    db.setUser(u);

                    //avviso l'utente che il login è andato a buon fine
                    Toast.makeText(MainActivity6.this,"login successful",Toast.LENGTH_LONG).show();
                    debug.setText(usr+" "+psw+db.User_logged);
                }

                //se l'utente inserito non è nel database avviso l'utente che username e/o password sono sbagliati
                else {
                    Toast.makeText(MainActivity6.this,"Wrong Username or Password",Toast.LENGTH_LONG).show();
                }

                //siccome nel passare da una pagina all'altra le informazioni relative ai pulsanti,testo ecc...
                //non vengono salvate quello che faccio è salvare tali informazioni in una struttura chiamata SHARED_PREFERENCE


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


    //funzioni che salvano, caricano e applicano lo stato dei pulsanti,testo ecc...


    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(DATABASE,db.toString());
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);

        db_s = sharedPreferences.getString(DATABASE, "UL:none\n");
    }

    public void updateView(){
        db = new DB(db_s);
        debug2.setText(db_s);
    }


}