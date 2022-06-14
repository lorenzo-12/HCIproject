package com.example.hciproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    public String user_img_path;
    public String current_date;
    public Integer dialog_filter = 0;
    Boolean FirstAccess = false;

    //variabili globali usate dalla Main page
    TextView database;
    ImageView userView;
    Button debug,button;
    FloatingActionButton add;
    DBHelper db;
    BottomNavigationView nav;

    //diary = username | date | or | food | exercise | quantity | sets | reps
    ArrayList<String> username_list, date_list,or_list, food_list, exercise_list, quantity_list, sets_list,reps_list;
    ArrayList<Bitmap> food_img_list;
    RecyclerView recyclerView;
    CustomAdapterDiaryFood customAdapterDiaryFood;

    Dialog dialog;
    ArrayList<String> dialog_food_list;
    ArrayList<Bitmap> dialog_food_img_list;
    EditText dialog_quantity;
    RecyclerView dialogRecycleView;
    CustomAdapterDialogFood customAdapterDialogFood;
    Button dialog_close,dialog_ok;
    ImageView dialog_food_btn,dialog_exercise_btn;

    @Override
    public void onResume() {
        // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        loadData();
        if (user_logged.equals("none")) userView.setImageResource(R.drawable.no_image2);
        else {
            debug.setText("USER: "+user_logged);
            userView.setImageBitmap(db.loadImage(user_logged));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        userView = findViewById(R.id.userbutton);
        debug = findViewById(R.id.buttondebug);
        database = findViewById(R.id.databse);
        add = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recycleViewDiet);
        button = findViewById(R.id.button);
        Calendar c = Calendar.getInstance();
        current_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        database.setText(db.viewUsers()+current_date);

        nav = findViewById(R.id.bottomnavigatorview);
        //così quando apro l'app mi da fin  da subito selezionata l'icona del diario
        nav.setSelectedItemId(R.id.bottom_diary);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_diary:
                        Toast.makeText(MainActivity.this,"diary",Toast.LENGTH_SHORT).show();
                        //do nothing since we are already in the Diary activity
                        return true;
                    case R.id.bottom_exercise:
                        Toast.makeText(MainActivity.this,"exercise",Toast.LENGTH_SHORT).show();
                        openactivityexercise();
                        return true;
                    case R.id.bottom_food:
                        Toast.makeText(MainActivity.this,"food",Toast.LENGTH_SHORT).show();
                        openactivityFood();
                        return true;
                    case R.id.bottom_time:
                        Toast.makeText(MainActivity.this,"timer",Toast.LENGTH_SHORT).show();
                        openactivitytimer();
                        return true;
                    case R.id.bottom_user:
                        Toast.makeText(MainActivity.this,"user",Toast.LENGTH_SHORT).show();
                        openactivityuser();
                        return true;
                }
                return true;
            }
        });

        //TODO
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
                Boolean check = db.deleteFoodFromDiary(user_logged,current_date,"apples");
                Cursor c = db.findAllUserFoodFromDiary(user_logged,current_date);
                storeDataInArrays();
                customAdapterDiaryFood.notifyDataSetChanged();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dialog)));

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialog_ok = dialog.findViewById(R.id.dialog_ok_btn);
        dialog_close = dialog.findViewById(R.id.dialog_close_btn);
        dialog_food_btn = dialog.findViewById(R.id.dialog_food_imgview);
        dialog_exercise_btn = dialog.findViewById(R.id.dialog_exercise_imgview);

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog_food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_filter = 0;
                setDialogFilter();
            }
        });

        dialog_exercise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_filter = 1;
                setDialogFilter();
            }
        });





        loadData();
        debug.setText("User: "+user_logged);

        if (!user_logged.equals("none")) {
            userView.setImageBitmap(db.loadImage(user_logged));
        }
        else {
            openactivityFitlife();
            userView.setImageResource(R.drawable.no_image2);
        }

        if (FirstAccess == false) {
            startThreadFood();
            startThreadExercise();
        }

        buildRecyclerView();
        storeDataInArrays();

    }

    public void setDialogFilter(){
        if (dialog_filter==0) {
            dialog_food_btn.setBackgroundColor(getResources().getColor(R.color.blue));
            dialog_exercise_btn.setBackgroundColor(getResources().getColor(R.color.dialog));
        }
        else {
            dialog_exercise_btn.setBackgroundColor(getResources().getColor(R.color.red));
            dialog_food_btn.setBackgroundColor(getResources().getColor(R.color.dialog));
        }
        storeDialogDataInArrays();
    }

    public void showDialog(){
        dialogRecycleView = dialog.findViewById(R.id.dialog_recycleview);
        dialog_quantity = dialog.findViewById(R.id.dialog_quantity_txt);

        buildDialogRecyclerView();
        storeDialogDataInArrays();
        dialog.show();
    }

    public void buildDialogRecyclerView(){
        db = new DBHelper(this);
        dialog_food_list = new ArrayList<String>();
        dialog_food_img_list = new ArrayList<Bitmap>();

        customAdapterDialogFood = new CustomAdapterDialogFood(MainActivity.this,dialog_food_list,dialog_food_img_list);
        dialogRecycleView.setAdapter(customAdapterDialogFood);
        dialogRecycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        customAdapterDialogFood.setOnItemClickListener(new CustomAdapterDialogFood.OnItemClickListener() {
            @Override
            public void onQuantityClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,"clicked: "+index,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,food_name_list.get(position).toString(),Toast.LENGTH_SHORT).show();
                storeDataInArrays();
                customAdapterDiaryFood.notifyDataSetChanged();
            }
        });

        customAdapterDiaryFood.notifyDataSetChanged();
    }

    public void storeDialogDataInArrays(){
        Cursor cursor = null;
        if (dialog_filter==0) cursor = db.readAllDataFood();
        if (dialog_filter==1) cursor = db.readAllDataExercise();
        dialog_food_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            while ((cursor != null) && (cursor.moveToNext())){
                dialog_food_list.add(cursor.getString(0).toLowerCase());
            }
        }
        customAdapterDialogFood.notifyDataSetChanged();
    }

    public void storeDataInArrays(){
        Cursor cursor = null;
        cursor = db.findAllUserFoodFromDiary(user_logged, current_date);
        Toast.makeText(MainActivity.this,"aaa: "+String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
        username_list.clear();
        date_list.clear();
        or_list.clear();
        food_list.clear();
        exercise_list.clear();
        quantity_list.clear();
        food_img_list.clear();
        sets_list.clear();
        reps_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            while ((cursor != null) && (cursor.moveToNext())){
                username_list.add(cursor.getString(0).toLowerCase());
                date_list.add(cursor.getString(1).toLowerCase());
                or_list.add(cursor.getString(2).toLowerCase());
                food_list.add(cursor.getString(3).toLowerCase());
                exercise_list.add(cursor.getString(4).toLowerCase());
                quantity_list.add(cursor.getString(5).toLowerCase());
                sets_list.add(cursor.getString(6).toLowerCase());
                reps_list.add(cursor.getString(7).toLowerCase());
            }
        }

    }

    public void buildRecyclerView(){
        db = new DBHelper(this);
        username_list = new ArrayList<String>();
        date_list = new ArrayList<String>();
        or_list = new ArrayList<String>();
        food_list = new ArrayList<String>();
        exercise_list = new ArrayList<String>();
        quantity_list = new ArrayList<String>();
        sets_list = new ArrayList<String>();
        reps_list = new ArrayList<String>();
        food_img_list = new ArrayList<Bitmap>();

        customAdapterDiaryFood = new CustomAdapterDiaryFood(MainActivity.this, username_list, date_list,or_list, food_list, exercise_list, quantity_list, sets_list,reps_list, food_img_list);
        recyclerView.setAdapter(customAdapterDiaryFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        customAdapterDiaryFood.setOnItemClickListener(new CustomAdapterDiaryFood.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,"clicked: "+index,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                String index = String.valueOf(position);
                //Toast.makeText(FoodPage.this,food_name_list.get(position).toString(),Toast.LENGTH_SHORT).show();
                Boolean result = db.deleteFoodFromDiary(user_logged,current_date,food_list.get(position).toLowerCase());
                storeDataInArrays();
                customAdapterDiaryFood.notifyDataSetChanged();
            }

            @Override
            public void onUpdateClick(int position) {
                //Toast.makeText(FoodPage.this,"Update",Toast.LENGTH_SHORT).show();
                String name = username_list.get(position).toLowerCase();
                String date = date_list.get(position).toLowerCase();
                String or = or_list.get(position).toLowerCase();
                String food = food_list.get(position).toLowerCase();
                String exercise = exercise_list.get(position).toLowerCase();
                String quantity = quantity_list.get(position).toLowerCase();
                String sets = sets_list.get(position).toLowerCase();
                String reps = reps_list.get(position).toLowerCase();
                return;
            }
        });

        customAdapterDiaryFood.notifyDataSetChanged();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(currentDateString);
        //TODO
        database = findViewById(R.id.databse);
        current_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        database.setText(current_date);
    }


    //relative funzioni che vengono chiamete quando premiamo un bottone
    public void openactivityDiary(){
        saveData();
        Intent intentDiary = new Intent(this, MainActivity.class);
        startActivity(intentDiary);
    }

    public void openactivityFitlife(){
        saveData();
        Intent intentFitlife = new Intent(this, FitlifePage.class);
        startActivity(intentFitlife);
    }

    public void openactivityFood(){
        saveData();
        Toast.makeText(MainActivity.this,"Loading",Toast.LENGTH_LONG).show();
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

    public void openactivityuser() {
        saveData();
        Intent intentUser;
        //creo la nuova pagina (intentUser)
        intentUser = new Intent(this, UserPage.class);
        //se non mi interessa ricevere delle informazioni dalla pagina figlia allora posso usare
        //direttamente STARTACTIVITY
        startActivity(intentUser);

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
        FoodThread t = new FoodThread();
        t.start();
    }

    public void startThreadExercise(){
        ExerciseThread t = new ExerciseThread();
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