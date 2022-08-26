package com.example.hciproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class FitlifePage extends AppCompatActivity {

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    Boolean FirstAccess = false;

    Button login,signup;
    DBHelper db;


    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;


    //codice per fare si che quando un utente clicca fuori da un Edittext si perde il focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitlifepage);

        login = findViewById(R.id.fitlife_login_button);
        signup = findViewById(R.id.fitlife_signup_button);
        db = new DBHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivityLogin();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivitySignup();
            }
        });

        loadData();

        if (!user_logged.equals("none")) {
            saveData();
            openactivityMain();
        }

        if (FirstAccess == false) {
            startThreadFood();
            startThreadExercise();
        }

        signin = findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.d("GOOGLE: ","login: OK");
            Intent intentgoogle = new Intent(FitlifePage.this, TestGoogle.class);
            startActivity(intentgoogle);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("GOOGLE: ","login: FAIL");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }

    public void openactivityLogin(){
        saveData();
        Intent intentLogin = new Intent(this, LoginPage.class);
        startActivity(intentLogin);
    }

    public void openactivitySignup(){
        saveData();
        Intent intentSignup = new Intent(this, SignupInfoPage.class);
        startActivity(intentSignup);
    }

    public void openactivityMain(){
        saveData();
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGGED,user_logged);
        editor.putBoolean("FirstAccess", FirstAccess);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_ACTIVITY", MODE_PRIVATE);
        user_logged = sharedPreferences.getString(USER_LOGGED, "none");
        FirstAccess = sharedPreferences.getBoolean("FirstAccess",false);
    }

    public void startThreadFood(){
        FitlifePage.FoodThread t = new FitlifePage.FoodThread();
        t.start();
    }

    public void startThreadExercise(){
        FitlifePage.ExerciseThread t = new FitlifePage.ExerciseThread();
        t.start();
    }

    class FoodThread extends Thread{
        @Override
        public void run() {
            db.addExistingFood();
            FirstAccess = true;
            saveData();
        }
    }

    class ExerciseThread extends Thread{
        @Override
        public void run() {
            db.addExistingExercise();
            FirstAccess = true;
            saveData();
        }
    }

}