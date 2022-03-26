package com.example.hciproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //variabili globali usate dalla Main page
    Button workoutbtn,diarybtn,dietbtn,timerbtn;
    ImageButton userbtn;
    ConstraintLayout layout;
    Button debug;

    //database contenente gli utenti registrati
    DB db = new DB();

    //variabili temporanea usate per debug
    User tmp = new User("admin","admin");
    ArrayList<User> lista = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //associo a ogni variabile il corrispettivo bottone/testo ecc...
        layout = findViewById(R.id.constraintLayout);
        layout.setBackgroundColor(Color.WHITE);
        workoutbtn = findViewById(R.id.WorkoutButton);
        diarybtn = findViewById(R.id.DiaryButton);
        dietbtn = findViewById(R.id.DietButton);
        timerbtn = findViewById(R.id.TimerButton);
        userbtn = findViewById(R.id.userbutton);
        debug = findViewById(R.id.buttondebug);

        //istruzioni utili per debug
        //aggiungo all'interno del database un utente temporaneo
        lista.add(tmp);
        db.addUser(tmp);


        //setto per ogni bottone la rispttiva funzione ONCLICK

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openactivityuser();}
        });

        diarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiary();
            }
        });

        dietbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityDiet();
            }
        });

        workoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityworkout();
            }
        });

        timerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivitytimer();
            }
        });


        //codice utilizzato per capire se quando aggiungo un utente dalla pagina (activity) di login i cambiamenti
        //fatti al database vengono riportati per intero oppure no
        //in questo caso utilizzo una casella di testo per mostrare se effettivamente il numero di User nel databese
        //incrementa oppure no (poichè non posso fare la print)
        debug.setText("***"+db.users_list.size()+"***");

    }

    //relative funzioni che vengono chiamete quando premiamo un bottone
    public void openactivityDiary(){
        Intent intentDiary = new Intent(this, MainActivity2.class);
        startActivity(intentDiary);
    }

    public void openactivityDiet(){
        Intent intentDiet = new Intent(this, MainActivity3.class);
        startActivity(intentDiet);
    }

    public void openactivityworkout(){
        Intent intentWorkout = new Intent(this, MainActivity4.class);
        startActivity(intentWorkout);
    }

    public void openactivitytimer(){
        Intent intentTimer = new Intent(this, MainActivity5.class);
        startActivity(intentTimer);
    }

    public void openactivityuser() {

        //creo la nuova pagina (intentUser)
        Intent intentUser = new Intent(this, MainActivity6.class);
        //per poter passare delle informazioni/variabili da una pagina all'altra si può
        //usare il PUTEXTRA che prende in input <KEY,DATA> in modo che se inviamo
        //più dati è possibile distinguerli tramite la KEY
        intentUser.putExtra("DB",db);

        //se non mi interessa ricevere delle informazioni dalla pagina figlia allora posso usare
        //direttamente STARTACTIVITY
        //startActivity(intentUser);

        //se invece sono interessato anche a ricevere delle informazioni indietro
        //quindi la comunicazione non è solo PADRE --> FIGLIO ma anche FIGLIO --> PADRE
        //allora uso questa funzione che mi permette di riottenere il risultato ottenuto dal FIGLIO
        //teoricamente dovrei usare un'altra funzione siccome questa è deprecatea, ma l'altra funzione
        //possibile è complicata e non mi va di perderci 2 giorni, inoltre questa funziona benissimo
        startActivityForResult(intentUser,1);

    }

    //funzione di callback che si attiva automanticamente quando il una pagina FIGLIA restituisce un valore
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){

                //in generale quando si  passa un messagio basta usare GETEXTRA ma siccome noi non stiamo inviando
                //una semplice stringa ma stiamo passando una variabile (puntatore) allora dobbiamo usare
                //GETPARCELABLEEXTRA che ci permette di ricevere una variabile

                //quello che faccio è semplicemente modificare il database ponendolo uguale a quello in output
                //dalla pagina FIGLIA
                db = data.getParcelableExtra("DB");

                //modifico la scritta nel testo di debug per capire se i cambiamenti sono avvenuti correttamente
                debug.setText(db.users_list.size()+" "+db.User_logged);

            }
        }
    }
}