package com.example.hciproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeUsernamePage extends AppCompatActivity {

    //variabili globali usate dalla pagina User per il login
    EditText oldusername,newusername;
    Button logout,reset;
    TextView debug;

    //variabili necessarie per salvare lo stato dei pulsanti,testo ecc...
    //poichè se un pulsante lo setto a NON CLICCABILE ma poi cambio pagina tale cambiamento
    //non rimane salvato, quindi lo devo fare manualmente
    public static final String DATABASE = "database";
    public String db_s;

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
        setContentView(R.layout.activity_change_username_page);

        logout = findViewById(R.id.logoutbtn);
        reset = findViewById(R.id.resetbtn);
        oldusername = findViewById(R.id.oldusernametxt);
        newusername = findViewById(R.id.newusernametxt);
        debug = findViewById(R.id.debug);

        oldusername.addTextChangedListener(resetTextWatcher);
        newusername.addTextChangedListener(resetTextWatcher);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadData();
        updateView();
    }

    public TextWatcher resetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void saveData(){
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(DATABASE,db.toString());
        editor.apply();
         */
    }

    public void loadData(){
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);

        db_s = sharedPreferences.getString(DATABASE, "UL:none\n");
         */
    }

    public void updateView(){
        /*
        db = new DB(db_s);
         */
    }
}