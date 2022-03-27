package com.example.hciproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE = "database";
    public String database_tostring;


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
    public void onResume() {
        // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        loadData();
        updateView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //associo a ogni variabile il corrispettivo bottone/testo ecc...
        layout = findViewById(R.id.constraintLayout);
        //layout.setBackgroundColor(Color.WHITE);
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


        loadData();
        updateView();

        //codice utilizzato per capire se quando aggiungo un utente dalla pagina (activity) di login i cambiamenti
        //fatti al database vengono riportati per intero oppure no
        //in questo caso utilizzo una casella di testo per mostrare se effettivamente il numero di User nel databese
        //incrementa oppure no (poichè non posso fare la print)
        debug.setText(db.users_list.size()+" "+db.User_logged);


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
        saveData();
        startActivity(intentTimer);
    }

    public void openactivityuser() {

        //creo la nuova pagina (intentUser)
        Intent intentUser = new Intent(this, MainActivity6.class);

        saveData();
        //se non mi interessa ricevere delle informazioni dalla pagina figlia allora posso usare
        //direttamente STARTACTIVITY
        startActivity(intentUser);

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(DATABASE,db.toString());
        editor.apply();
        //Toast.makeText(this,"DATA_SAVED", Toast.LENGTH_LONG).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY",MODE_PRIVATE);
        database_tostring = sharedPreferences.getString(DATABASE, "UL;none\nU;admin;admin\n");
    }

    public void updateView(){
        db = new DB(database_tostring);
        debug.setText(db.users_list.size()+" "+db.User_logged);
    }
}