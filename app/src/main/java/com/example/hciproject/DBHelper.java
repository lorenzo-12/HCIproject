package com.example.hciproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "mydatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";

    public static final String FOOD_TABLE = "food";
    public static final String CNAME_FOOD = "food_name";
    public static final String CCATEGORY_FOOD = "food_category";
    public static final String CCARB = "carb";
    public static final String CPROT = "prot";
    public static final String CFAT = "fat";

    public static final String WORKOUT_TABLE = "workout";
    public static final String CNAME_WORKOUT = "workout_name";
    public static final String CCATEGORY_WORKOUT = "workout_category";
    public static final String CREPS_WORKOUT = "workout_reps";
    public static final String CSERIES_WORKOUT = "workout_series";

    public static final String CREATE_USER_TABLE = "CREATE TABLE "+USER_TABLE+" ("+CUSERNAME+" TEXT PRIMARY KEY, "+CPASSWORD+" TEXT NOT NULL);";
    public static final String CREATE_FOOD_TABLE = "CREATE TABLE "+FOOD_TABLE+" ("+CNAME_FOOD+" TEXT PRIMARY KEY, "+CCATEGORY_FOOD+" TEXT, "+CCARB+" INTEGER, "+CPROT+" INTEGER, "+CFAT+" INTEGER );";
    public static final String CREATE_WORKOUT_TABLE = "CREATE TABLE "+WORKOUT_TABLE+" ("+CNAME_WORKOUT+" TEXT PRIMARY KEY, "+CCATEGORY_WORKOUT+" TEXT, "+CREPS_WORKOUT+" INTEGER, "+CSERIES_WORKOUT+" INTEGER);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_WORKOUT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ FOOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ WORKOUT_TABLE);
    }

    public Boolean addWorkout(String name,String category,int reps, int series){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CNAME_WORKOUT,name);
        cv.put(CCATEGORY_WORKOUT,category);
        cv.put(CREPS_WORKOUT,reps);
        cv.put(CSERIES_WORKOUT,series);

        long result = db.insert(WORKOUT_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean updateWorkout(String name,String category,int reps,int series){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CCATEGORY_WORKOUT,category);
        cv.put(CREPS_WORKOUT,reps);
        cv.put(CSERIES_WORKOUT,series);
        Cursor cursor = db.rawQuery("SELECT * FROM " + WORKOUT_TABLE + " WHERE workout_name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.update(WORKOUT_TABLE, cv, "workout_name=?", new String[]{name});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean deleteWorkout(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WORKOUT_TABLE + " WHERE workout_name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.delete(WORKOUT_TABLE,"workout_name=?", new String[]{name});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor readAllDataWorkout(){
        String  query = "SELECT * FROM "+ WORKOUT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Boolean findWorkout(String name){
        String query = "SELECT * FROM "+WORKOUT_TABLE+" WHERE workout_name=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{name});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public String viewWorkouts(){
        Cursor cursor = readAllDataWorkout();
        if(cursor.getCount() == 0) return "No Data";
        String res = "";
        while (cursor.moveToNext()){
            res += cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+"\n";
        }
        return res;
    }

    public Boolean addFood(String name, String category, int carb, int prot, int fat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CNAME_FOOD,name);
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);

        long result = db.insert(FOOD_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean updateFood(String name,String category,int carb,int prot,int fat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);
        Cursor cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE + " WHERE name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.update(FOOD_TABLE, cv, "food_name=?", new String[]{name});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean deleteFood(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE + " WHERE food_name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.delete(FOOD_TABLE,"food_name=?", new String[]{name});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor readAllDataFood(){
        String  query = "SELECT * FROM "+ FOOD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Boolean findFood(String name){
        String query = "SELECT * FROM "+FOOD_TABLE+" WHERE food_name=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{name});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public String viewFoods(){
        Cursor cursor = readAllDataFood();
        if(cursor.getCount() == 0) return "No Data";
        String res = "";
        while (cursor.moveToNext()){
            res += cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4)+"\n";
        }
        return res;
    }

    public Boolean addUser(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CUSERNAME,username);
        cv.put(CPASSWORD,password);

        long result = db.insert(USER_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean updateUserPassword(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CPASSWORD,password);
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE username = ? ", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = db.update(USER_TABLE, cv, "username=?", new String[]{username});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean deleteUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE name = ? ", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = db.delete(USER_TABLE,"username=?", new String[]{username});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor readAllDataUser(){
        String  query = "SELECT * FROM "+ USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Boolean findUser(String username){
        String query = "SELECT * FROM "+USER_TABLE+" WHERE username=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public Boolean checkUser(String username,String password){
        String query = "SELECT * FROM "+USER_TABLE+" WHERE username = ? AND password = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{username,password});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public String viewUsers(){
        Cursor cursor = readAllDataUser();
        if(cursor.getCount() == 0) return "No Data";
        String res = "";
        while (cursor.moveToNext()){
            res += cursor.getString(0)+" "+cursor.getString(1)+"\n";
        }
        return res;
    }

}
