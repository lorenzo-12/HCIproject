package com.example.hciproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        //inizializzo il databse ponendo uguale a quello che mi è stato passato
        //dalla pagina PADRE (main page)
        db = getIntent().getParcelableExtra("DB");

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
                        Intent i = new Intent();
                        //mando alla pagina padre (main page) il Database modificato
                        i.putExtra("DB",db);
                        setResult(RESULT_OK,i);
                        //mostro un messaggio in cui notifico all'utente che la procedura è andata a buon fine
                        Toast.makeText(MainActivity6.this,"SignUp successful",Toast.LENGTH_LONG).show();

                        //codice necessario per far si che ci sia un minimo di delay nel passggio dalla pagina di login alla main page
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //finish server poichè senza di essa non viene effettivamente mandato indietro il messaggio con il database
                                //finish inoltre stoppa la pagina corrente e torna a quella precedente
                                MainActivity6.this.finish();
                            }
                        }, 500);
                    }

                    //se invece lo Username inserito esiste già allora notifico l'utente di ciò
                    else if (different_username == false){
                        Toast.makeText(MainActivity6.this,"Username alredy used, please try another username",Toast.LENGTH_LONG).show();
                    }
                }
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
                Intent i;

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
                    i = new Intent();
                    i.putExtra("DB",db);
                    setResult(RESULT_OK,i);

                    //avviso l'utente che il login è andato a buon fine
                    Toast.makeText(MainActivity6.this,"login successful",Toast.LENGTH_LONG).show();
                    //codice necessario per far si che ci sia un minimo di delay nel passggio dalla pagina di login alla main page
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //finish server poichè senza di essa non viene effettivamente mandato indietro il messaggio con il database
                            //finish inoltre stoppa la pagina corrente e torna a quella precedente
                            MainActivity6.this.finish();
                        }
                    }, 500);
                }

                //se l'utente inserito non è nel database avviso l'utente che username e/o password sono sbagliati
                else {
                    Toast.makeText(MainActivity6.this,"Wrong Username or Password",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}