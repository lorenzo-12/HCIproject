package com.example.hciproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String CARB_S = "SINGUP_CARB";
    public static final String PROT_S = "SINGUP_PROT";
    public static final String FAT_S = "SINGUP_FAT";
    public static final String KAL_S = "SINGUP_KAL";
    Integer carb_value,prot_value,fat_value, cal_value;

    public static final String USER_LOGGED = "user_logged";
    public String user_logged;
    public String user_img_path;
    public String current_date;
    public Integer dialog_filter = 0;
    Boolean FirstAccess = false;
    public Map<String, ArrayList<String>> map = new HashMap<String,ArrayList<String>>();

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
    ArrayList<String> dialog_item_list;
    ArrayList<Bitmap> dialog_item_img_list;
    EditText dialog_quantity;
    RecyclerView dialogRecycleView;
    CustomAdapterDialogItem customAdapterDialogItem;
    Button dialog_close,dialog_ok;
    ImageView dialog_food_btn,dialog_exercise_btn;

    Dialog dialog2;
    TextView dialog2_set_txt, dialog2_rep_txt, dialog2_quantity_txt;
    EditText dialog2_set, dialog2_rep, dialog2_quantity;
    Button dialog2_ok_btn, dialog2_back_btn;
    String user,date,item_name,value1,value2;
    Boolean type;

    int progress = 0;
    int total_cal,total_carb,total_prot,total_fat;
    ProgressBar progressBar,progressBar_carb,progressBar_prot,progressBar_fat;
    TextView text_cal,text_carb,text_prot,text_fat,text_value_carb,text_value_prot,text_value_fat,text_value_cal;


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
        setContentView(R.layout.activity_main);

        //View overlay = findViewById(R.id.constraintLayout);
        //overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

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

        progressBar = findViewById(R.id.CircularProgressBar);
        progressBar_carb = findViewById(R.id.progressBar_carb);
        progressBar_prot = findViewById(R.id.progressBar_prot);
        progressBar_fat = findViewById(R.id.progressBar_fat);
        text_cal = findViewById(R.id.calories_label);
        text_carb = findViewById(R.id.carb_label);
        text_prot = findViewById(R.id.protein_label);
        text_fat = findViewById(R.id.fat_label);
        text_value_carb = findViewById(R.id.carb_value_text);
        text_value_prot = findViewById(R.id.prot_value_text);
        text_value_fat = findViewById(R.id.fat_value_text);
        text_value_cal = findViewById(R.id.cal_value_text);


        nav = findViewById(R.id.bottomnavigatorview);
        //così quando apro l'app mi da fin  da subito selezionata l'icona del diario
        nav.setSelectedItemId(R.id.bottom_diary);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_diary:
                        //Toast.makeText(MainActivity.this,"diary",Toast.LENGTH_SHORT).show();
                        //do nothing since we are already in the Diary activity
                        return true;
                    case R.id.bottom_exercise:
                        //Toast.makeText(MainActivity.this,"exercise",Toast.LENGTH_SHORT).show();
                        openactivityexercise();
                        return true;
                    case R.id.bottom_food:
                        //Toast.makeText(MainActivity.this,"food",Toast.LENGTH_SHORT).show();
                        openactivityFood();
                        return true;
                    case R.id.bottom_time:
                        //Toast.makeText(MainActivity.this,"timer",Toast.LENGTH_SHORT).show();
                        openactivitytimer();
                        return true;
                    case R.id.bottom_user:
                        //Toast.makeText(MainActivity.this,"user",Toast.LENGTH_SHORT).show();
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
            }
        });
        button.setText(current_date);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dialog)));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                addMapItem();
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

        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dialog)));
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setContentView(R.layout.layout_custom_dialog2);

        dialog2_set_txt = dialog2.findViewById(R.id.dialog2_set_text);
        dialog2_rep_txt = dialog2.findViewById(R.id.dialog2_rep_text);
        dialog2_quantity_txt = dialog2.findViewById(R.id.dialog2_quantity_text);
        dialog2_set = dialog2.findViewById(R.id.dialog2_set_input);
        dialog2_rep = dialog2.findViewById(R.id.dialog2_rep_input);
        dialog2_quantity = dialog2.findViewById(R.id.dialog2_quantity_input);
        dialog2_ok_btn = dialog2.findViewById(R.id.dialog2_ok_btn);
        dialog2_back_btn = dialog2.findViewById(R.id.dialog2_back_btn);

        dialog2_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
                dialog2.dismiss();
            }
        });

        dialog2_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
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
        set_progress();
    }

    public void setDialogFilter(){
        if (dialog_filter==0) {
            dialog_food_btn.setBackgroundColor(getResources().getColor(R.color.celeste));
            dialog_exercise_btn.setBackgroundColor(getResources().getColor(R.color.dialog));
        }
        else {
            dialog_exercise_btn.setBackgroundColor(getResources().getColor(R.color.celeste));
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

    public void showDialog2(String user, String date, String item_name,Boolean type,String value1, String value2){
        if (type){
            dialog2_set_txt.setVisibility(View.INVISIBLE);
            dialog2_set_txt.setClickable(false);
            dialog2_set.setVisibility(View.INVISIBLE);
            dialog2_set.setClickable(false);

            dialog2_rep_txt.setVisibility(View.INVISIBLE);
            dialog2_rep_txt.setClickable(false);
            dialog2_rep.setVisibility(View.INVISIBLE);
            dialog2_rep.setClickable(false);

            dialog2_quantity_txt.setVisibility(View.VISIBLE);
            dialog2_quantity_txt.setClickable(true);
            dialog2_quantity.setVisibility(View.VISIBLE);
            dialog2_quantity.setClickable(true);

            dialog2_quantity.setText(value1);
        }
        else {
            dialog2_set_txt.setVisibility(View.VISIBLE);
            dialog2_set_txt.setClickable(true);
            dialog2_set.setVisibility(View.VISIBLE);
            dialog2_set.setClickable(true);

            dialog2_rep_txt.setVisibility(View.VISIBLE);
            dialog2_rep_txt.setClickable(true);
            dialog2_rep.setVisibility(View.VISIBLE);
            dialog2_rep.setClickable(true);

            dialog2_quantity_txt.setVisibility(View.INVISIBLE);
            dialog2_quantity_txt.setClickable(false);
            dialog2_quantity.setVisibility(View.INVISIBLE);
            dialog2_quantity.setClickable(false);

            dialog2_set.setText(value1);
            dialog2_rep.setText(value2);
        }
        dialog2.show();
    }

    public void updateItem(){
        Boolean a;
        String x = dialog2_set.getText().toString();
        String y = dialog2_rep.getText().toString();
        String z = dialog2_quantity.getText().toString();
        if (type==true) a=  db.modifyFoodFromDiary(user,date,item_name,Integer.parseInt(z));
        else a = db.modifyExerciseFromDiary(user,date,item_name,Integer.parseInt(x),Integer.parseInt(y));
        //Toast.makeText(this, "ddd: "+String.valueOf(a), Toast.LENGTH_SHORT).show();
        buildRecyclerView();
        storeDataInArrays();
        customAdapterDiaryFood.notifyDataSetChanged();
    }

    public void addMapItem(){
        //Log.d("aaaaaa",map.toString());
        for ( Map.Entry<String,ArrayList<String>> e : map.entrySet()) {
            //Log.d("aaaaaa",e.toString());
            //Toast.makeText(MainActivity.this, "item type: "+e.getValue().get(0), Toast.LENGTH_SHORT).show();
            if (e.getValue().get(0).equals("1")) db.addFoodToDiary(user_logged, current_date, e.getKey(), Integer.parseInt(e.getValue().get(1)));
            else db.addExerciseToDiary(user_logged, current_date, e.getKey(), Integer.parseInt(e.getValue().get(1)),Integer.parseInt(e.getValue().get(2)));
        }
        map.clear();
        buildRecyclerView();
        storeDataInArrays();
        customAdapterDiaryFood.notifyDataSetChanged();
    }


    public void updateMap(int position,CustomAdapterDialogItem.MyViewHolder v){
        String q_text = v.quantity_txt.getText().toString();
        String q2_txt = v.quantity2_txt.getText().toString();
        String q3_txt = v.quantity3_txt.getText().toString();
        String item_name = v.name_txt.getText().toString().toLowerCase();
        String cor = "1";
        if (db.findFood(item_name)) cor = "1";
        if (db.findExercise(item_name)) cor = "0";
        //Toast.makeText(MainActivity.this, "cor: "+cor, Toast.LENGTH_SHORT).show();

        //Log.d("UpdateMap - prima",map.toString());
        if (cor.equals("1")){
            //Toast.makeText(this, "q_txt: "+q_text, Toast.LENGTH_SHORT).show();
            if (map.containsKey(item_name)) {map.remove(item_name);}
            if (!q_text.equals("") && !q_text.equals("0")){
                ArrayList<String> tmp = new ArrayList<String>();
                tmp.add(cor);
                tmp.add(q_text);
                tmp.add(q2_txt);
                map.put(item_name,tmp);
            }
        }
        if (cor.equals("0")){
            //Toast.makeText(this, "q_txt: "+q_text+" q2_txt: "+q2_txt, Toast.LENGTH_SHORT).show();
            if (map.containsKey(item_name)) map.remove(item_name);
            if (!q3_txt.equals("") && !q2_txt.equals("") && !q3_txt.equals("0") && !q2_txt.equals("0")){
                ArrayList<String> tmp = new ArrayList<String>();
                tmp.add(cor);
                tmp.add(q3_txt);
                tmp.add(q2_txt);
                map.put(item_name,tmp);
            }
        }
        //Toast.makeText(this, "map size: "+ String.valueOf(map.size()), Toast.LENGTH_SHORT).show();
        //Log.d("UpdateMap - dopo",map.toString());
    }


    public void buildDialogRecyclerView(){
        db = new DBHelper(this);
        dialog_item_list = new ArrayList<String>();
        dialog_item_img_list = new ArrayList<Bitmap>();

        customAdapterDialogItem = new CustomAdapterDialogItem(MainActivity.this, dialog_item_list, dialog_item_img_list);
        dialogRecycleView.setAdapter(customAdapterDialogItem);
        dialogRecycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        customAdapterDialogItem.setOnItemClickListener(new CustomAdapterDialogItem.OnItemClickListener() {
            @Override
            public void onQuantityClick(int position, CustomAdapterDialogItem.MyViewHolder v) {
                updateMap(position,v);
            }

            @Override
            public void onQuantity2Click(int position, CustomAdapterDialogItem.MyViewHolder v) {
                updateMap(position,v);
            }

            @Override
            public void onQuantity3Click(int position, CustomAdapterDialogItem.MyViewHolder v) {
                updateMap(position,v);
            }

        });
        set_progress();
    }


    public void storeDialogDataInArrays(){
        Cursor cursor = null;
        //if (dialog_filter==0) cursor = db.readAllDataFood();
        //if (dialog_filter==1) cursor = db.readAllDataExercise();
        if (dialog_filter==0) cursor = db.findAllFoodNotInDiary(user_logged,current_date);
        if (dialog_filter==1) cursor = db.findAllExerciseNotInDiary(user_logged,current_date);
        dialog_item_list.clear();
        if ((cursor != null) && (cursor.getCount() == 0)){
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            while ((cursor != null) && (cursor.moveToNext())){
                dialog_item_list.add(cursor.getString(0).toLowerCase());
            }
        }
        customAdapterDialogItem.notifyDataSetChanged();
        set_progress();
    }

    public void storeDataInArrays(){
        Cursor cursor = null;
        cursor = db.findAllUserFoodFromDiary(user_logged, current_date);
        Cursor cursor2 = null;
        cursor2 = db.findAllUserExerciseFromDiary(user_logged,current_date);
        //Toast.makeText(MainActivity.this,"aaa: "+String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
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
        if ((cursor2 != null) && (cursor2.getCount() == 0)){
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            while ((cursor2 != null) && (cursor2.moveToNext())){
                username_list.add(cursor2.getString(0).toLowerCase());
                date_list.add(cursor2.getString(1).toLowerCase());
                or_list.add(cursor2.getString(2).toLowerCase());
                food_list.add(cursor2.getString(3).toLowerCase());
                exercise_list.add(cursor2.getString(4).toLowerCase());
                quantity_list.add(cursor2.getString(5).toLowerCase());
                sets_list.add(cursor2.getString(6).toLowerCase());
                reps_list.add(cursor2.getString(7).toLowerCase());
            }
        }
        customAdapterDiaryFood.notifyDataSetChanged();
        set_progress();
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
                //Toast.makeText(MainActivity.this,user_logged+" DELETE "+food_list.get(position).toLowerCase(),Toast.LENGTH_SHORT).show();
                if (or_list.get(position).equals("1")) {
                    db.deleteFoodFromDiary(user_logged, current_date, food_list.get(position).toLowerCase());
                }
                else {
                    db.deleteExerciseFromDiary(user_logged,current_date,exercise_list.get(position).toLowerCase());
                }
                buildRecyclerView();
                storeDataInArrays();
                customAdapterDiaryFood.notifyDataSetChanged();
            }

            @Override
            public void onUpdateClick(int position) {
                type = db.findFood(food_list.get(position));
                user = username_list.get(position);
                date = date_list.get(position);
                value1 = "0";
                value2 = "0";
                if (type) {
                    item_name = food_list.get(position);
                    value1 = quantity_list.get(position);
                }
                else {
                    item_name = exercise_list.get(position);
                    value1 = sets_list.get(position);
                    value2 = reps_list.get(position);
                }

                showDialog2(user,date,item_name,type,value1,value2);
                return;
            }

        });
        set_progress();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        button.setText(currentDateString);
        database = findViewById(R.id.databse);
        current_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        database.setText(current_date);
        storeDataInArrays();
        customAdapterDiaryFood.notifyDataSetChanged();
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
        carb_value = sharedPreferences.getInt(CARB_S,0);
        prot_value = sharedPreferences.getInt(PROT_S,0);
        fat_value = sharedPreferences.getInt(FAT_S,0);
        cal_value = sharedPreferences.getInt(KAL_S,0);
    }

    public void set_progress(){
        total_carb = 0;
        total_prot = 0;
        total_fat = 0;
        total_cal = 0;
        Cursor cursor = db.findAllUserFoodFromDiary(user_logged,current_date);
        if ((cursor != null) && (cursor.getCount() == 0)){
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(FoodPage.this,"No Data",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
            while ((cursor != null) && (cursor.moveToNext())){
                String food_name_string = cursor.getString(3).toString().toLowerCase();
                int food_quantity_int = cursor.getInt(5);
                ArrayList<Integer> food_info = db.getFoodInfo(food_name_string);
                total_carb = total_carb+food_info.get(0)*food_quantity_int/100;
                total_prot = total_prot+food_info.get(1)*food_quantity_int/100;
                total_fat = total_fat+food_info.get(2)*food_quantity_int/100;
                total_cal = total_cal+food_info.get(3)*food_quantity_int/100;
            }
        }
        text_carb.setText("CARBS ");
        text_value_carb.setText(String.valueOf(total_carb)+"/"+carb_value+"g");
        text_prot.setText("PROTEINS ");
        text_value_prot.setText(String.valueOf(total_prot)+"/"+prot_value+"g");
        text_fat.setText("FATS ");
        text_value_fat.setText(String.valueOf(total_fat)+"/"+fat_value+"g");
        text_cal.setText("CALORIES ");
        text_value_cal.setText(String.valueOf(total_cal)+"/"+cal_value+"kcal");
        progressBar.setMax(cal_value);
        progressBar.setProgress(total_cal);
        progressBar_carb.setMax(carb_value);
        progressBar_carb.setProgress(total_carb);
        progressBar_carb.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar_prot.setMax(prot_value);
        progressBar_prot.setProgress(total_prot);
        progressBar_prot.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar_fat.setMax(fat_value);
        progressBar_fat.setProgress(total_fat);
        progressBar_fat.getProgressDrawable().setColorFilter(
                Color.MAGENTA, android.graphics.PorterDuff.Mode.SRC_IN);
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
        //Toast.makeText(MainActivity.this,"Loading",Toast.LENGTH_LONG).show();
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
}