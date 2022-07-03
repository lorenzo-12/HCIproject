package com.example.hciproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "mydatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";
    public static final String CSEX = "sex";
    public static final String CWEIGHT = "weight";
    public static final String CHEIGHT = "height";
    public static final String CCARB_GOAL = "carbgoal";
    public static final String CPROT_GOAL = "protgoal";
    public static final String CFAT_GOAL = "fatgoal";
    public static final String CKAL_GOAL = "kalgoal";
    public static final String QUESTION = "question";

    public static final String FOOD_TABLE = "food";
    public static final String CNAME_FOOD = "food_name";
    public static final String CCATEGORY_FOOD = "food_category";
    public static final String CCARB = "carb";
    public static final String CPROT = "prot";
    public static final String CFAT = "fat";
    public static final String CKAL = "kal";

    public static final String EXERCISE_TABLE = "exercise";
    public static final String CNAME_EXERCISE = "exercise_name";
    public static final String CCATEGORY_EXERCISE = "exercise_category";
    public static final String CREPS_EXERCISE = "exercise_reps";
    public static final String CSERIES_EXERCISE = "exercise_series";


    //diary = username | date | or | food | exercise | quantity | sets | reps
    public static final String DIARY_TABLE = "diary";
    public static final String CNAME_USER = "username";
    public static final String CDATE = "date";
    // 1 stands for food, 0 stands for exercise
    public static final String COR = "cor";
    public static final String CFOOD = "food";
    public static final String CEXERCISE = "exercise";
    public static final String CQUANTITY = "quantity";
    public static final String CSETS = "sets";
    public static final String CREPS = "reps";


    public static final String CREATE_USER_TABLE = "CREATE TABLE "+USER_TABLE+" (  "+CUSERNAME+" TEXT PRIMARY KEY, "
                                                                                    +CPASSWORD+" TEXT NOT NULL, "
                                                                                    +CSEX+" INTEGER, "
                                                                                    +CWEIGHT+" INTEGER, "
                                                                                    +CHEIGHT+" INTEGER, "
                                                                                    +CCARB_GOAL+" INTEGER, "
                                                                                    +CPROT_GOAL+" INTEGER, "
                                                                                    +CFAT_GOAL+" INTEGER, "
                                                                                    +CKAL_GOAL+" INTEGER, "
                                                                                    +QUESTION+" TEXT );";

    public static final String CREATE_FOOD_TABLE = "CREATE TABLE "+FOOD_TABLE+" (  "+CNAME_FOOD+" TEXT PRIMARY KEY, "
                                                                                    +CCATEGORY_FOOD+" TEXT, "
                                                                                    +CCARB+" INTEGER, "
                                                                                    +CPROT+" INTEGER, "
                                                                                    +CFAT+" INTEGER,"
                                                                                    +CKAL+" INTEGER );";

    public static final String CREATE_EXERCISE_TABLE = "CREATE TABLE "+ EXERCISE_TABLE +" ("+ CNAME_EXERCISE +" TEXT PRIMARY KEY, "
                                                                                            + CCATEGORY_EXERCISE +" TEXT, "
                                                                                            + CREPS_EXERCISE +" INTEGER, "
                                                                                            + CSERIES_EXERCISE +" INTEGER );";

    public static final String CREATE_DIARY_TABLE = "CREATE TABLE "+DIARY_TABLE+" (  "  +CNAME_USER+" TEXT NOT NULL, "
                                                                                        +CDATE+" TEXT NOT NULL, "
                                                                                        +COR+" INTEGER, "
                                                                                        +CFOOD+" TEXT NOT NULL, "
                                                                                        +CEXERCISE+" TEXT NOT NULL, "
                                                                                        +CQUANTITY+" INTEGER, "
                                                                                        +CSETS+" INTEGER, "
                                                                                        +CREPS+" INTEGER, "
                                                                                        +"CONSTRAINT PK_Diary PRIMARY KEY ("+CNAME_USER+","+CDATE+","+CFOOD+","+CEXERCISE+"));";


    public DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    public void saveImage(Bitmap bitmap, String name){
        name = name + ".png";
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(String name){
        name = name + ".png";
        try{
            File f = new File(name);
            f.delete();
        }catch (Exception e){

        }
    }

    public Bitmap loadImage(String name){
        name = name + ".png";
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try{
            fileInputStream = context.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_DIARY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ FOOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ EXERCISE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ DIARY_TABLE);
    }

    public Boolean addFoodToDiary(String username, String date, String food, int q){
        boolean exist = findFoodFromDiary(username,date, food);
        if (exist == true) return modifyFoodFromDiary(username,date,food,q);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CNAME_USER,username.toLowerCase());
        cv.put(CDATE,date.toLowerCase());
        cv.put(COR,1);
        cv.put(CFOOD,food.toLowerCase());
        cv.put(CEXERCISE, "");
        cv.put(CQUANTITY, q);
        cv.put(CSETS, 0);
        cv.put(CREPS, 0);

        long result = db.insert(DIARY_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean addExerciseToDiary(String username, String date, String exercise, int s, int r){

        boolean exist = findExerciseFromDiary(username, date,exercise);
        if (exist == true) return modifyExerciseFromDiary(username,date,exercise,s,r);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CNAME_USER,username.toLowerCase());
        cv.put(CDATE,date.toLowerCase());
        cv.put(COR,0);
        cv.put(CFOOD,"");
        cv.put(CEXERCISE, exercise.toLowerCase());
        cv.put(CQUANTITY, 0);
        cv.put(CSETS, s);
        cv.put(CREPS, r);

        long result = db.insert(DIARY_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean deleteFoodFromDiary(String username, String date, String food){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DIARY_TABLE + " WHERE username = ? AND food = ? AND date = ? ", new String[]{username, food, date.toLowerCase()});
        if (cursor.getCount() > 0) {
            long result = db.delete(DIARY_TABLE," username = ? AND food = ? AND date = ? ", new String[]{username, food, date.toLowerCase()});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean deleteExerciseFromDiary(String username, String date, String exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DIARY_TABLE + " WHERE username = ? AND exercise = ? AND date = ? ", new String[]{username, exercise, date.toLowerCase()});
        if (cursor.getCount() > 0) {
            long result = db.delete(DIARY_TABLE," username = ? AND exercise = ? AND date = ? ", new String[]{username, exercise, date.toLowerCase()});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor findAllUserFoodFromDiary(String username, String data){
        String query = "SELECT * FROM "+DIARY_TABLE+" WHERE username = ? AND date = ? AND cor = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{username.toLowerCase(), data.toLowerCase()});
        return cursor;
    }

    public Cursor findAllUserExerciseFromDiary(String username, String data){
        String query = "SELECT * FROM "+DIARY_TABLE+" WHERE username = ? AND date = ? AND cor = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{username.toLowerCase(), data.toLowerCase()});
        return cursor;
    }

    public Cursor findAllFoodNotInDiary(String username, String data){
        String query = "SELECT * FROM "+FOOD_TABLE+" WHERE "+CNAME_FOOD+" NOT IN ( SELECT "+CFOOD+" FROM "+DIARY_TABLE+" WHERE username = ? AND date = ?  ) ORDER BY "+CNAME_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[] {username.toLowerCase(),data.toLowerCase()});
        return cursor;
    }

    public Cursor findAllExerciseNotInDiary(String username, String data){
        String query = "SELECT * FROM "+EXERCISE_TABLE+" WHERE "+CNAME_EXERCISE+" NOT IN ( SELECT "+CEXERCISE+" FROM "+DIARY_TABLE+" WHERE username = ? AND date = ? ) ORDER BY "+CNAME_EXERCISE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[] {username.toLowerCase(),data.toLowerCase()});
        return cursor;
    }

    public Boolean findFoodFromDiary(String username, String data, String food){
        String query = "SELECT * FROM "+DIARY_TABLE+" WHERE username = ? AND food = ? AND date = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{username, food, data.toLowerCase()});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public Boolean findExerciseFromDiary(String username, String data, String exercise){
        String query = "SELECT * FROM "+DIARY_TABLE+" WHERE username = ? AND exercise = ? AND date = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{username, exercise, data.toLowerCase()});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public Boolean modifyFoodFromDiary(String username,String date,String food, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CQUANTITY, quantity);

        Cursor cursor = db.rawQuery("SELECT * FROM " + DIARY_TABLE + " WHERE username = ? AND food = ? AND date = ? ", new String[]{username, food, date.toLowerCase()});
        if (cursor.getCount() > 0) {
            long result = db.update(DIARY_TABLE, cv, "username = ? AND food = ? AND date = ?", new String[]{username, food, date.toLowerCase()});
            if (result == -1) return false;
            return true;
        }
        else return false;

    }

    public Boolean modifyExerciseFromDiary(String username,String date, String exercise, int s, int r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CREPS, r);
        cv.put(CSETS, s);

        Cursor cursor = db.rawQuery("SELECT * FROM " + DIARY_TABLE + " WHERE username = ? AND exercise = ? AND date = ? ", new String[]{username, exercise, date.toLowerCase()});
        if (cursor.getCount() > 0) {
            long result = db.update(DIARY_TABLE, cv, "username = ? AND exercise = ? AND date = ?", new String[]{username, exercise, date.toLowerCase()});
            if (result == -1) return false;
            return true;
        }
        else return false;
    }

    /*
    ***********************************************
     */

    public Boolean addExercise(String name, String category, int reps, int series){
        boolean exist = findFood(name);
        if (exist == true) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Bitmap img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.no_image2);
        saveImage(img_bitmap,name.toLowerCase());

        cv.put(CNAME_EXERCISE,name.toLowerCase());
        cv.put(CCATEGORY_EXERCISE,category);
        cv.put(CREPS_EXERCISE,reps);
        cv.put(CSERIES_EXERCISE,series);

        long result = db.insert(EXERCISE_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean addExercise(String name, String category, int reps, int series,Bitmap img){
        boolean exist = findFood(name);
        if (exist == true) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        saveImage(img,name.toLowerCase());

        cv.put(CNAME_EXERCISE,name.toLowerCase());
        cv.put(CCATEGORY_EXERCISE,category);
        cv.put(CREPS_EXERCISE,reps);
        cv.put(CSERIES_EXERCISE,series);

        long result = db.insert(EXERCISE_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean deleteExercise(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXERCISE_TABLE + " WHERE exercise_name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.delete(EXERCISE_TABLE,"exercise_name=?", new String[]{name});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor readAllDataExercise(){
        String  query = "SELECT * FROM "+ EXERCISE_TABLE + " ORDER BY "+CNAME_EXERCISE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readFilteredExerciseByName(String name_prefix){
        String query = "SELECT * FROM "+EXERCISE_TABLE+" WHERE exercise_name LIKE '"+name_prefix+"%';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,new String[]{});
        }
        return cursor;
    }

    public Cursor readFilteredExerciseByCategory(String filter){
        String query = "SELECT * FROM "+ EXERCISE_TABLE +" WHERE exercise_category=?"+ " ORDER BY "+CNAME_EXERCISE;;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,new String[]{filter});
        }
        return cursor;
    }

    public Boolean findExercise(String name){
        String query = "SELECT * FROM "+ EXERCISE_TABLE +" WHERE exercise_name=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return false;
        Cursor cursor = db.rawQuery(query, new String[]{name});
        if(cursor.getCount() == 0) return false;
        return true;
    }

    public String viewWorkouts(){
        Cursor cursor = readAllDataExercise();
        if(cursor.getCount() == 0) return "No Data";
        String res = "";
        while (cursor.moveToNext()){
            res += cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+"\n";
        }
        return res;
    }

    public Boolean addFood(String name, String category, int carb, int prot, int fat,int kal){
        boolean exist = findFood(name);
        if (exist == true) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Bitmap img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.no_image2);
        saveImage(img_bitmap,name.toLowerCase());

        cv.put(CNAME_FOOD,name.toLowerCase());
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);
        cv.put(CKAL,kal);

        long result = db.insert(FOOD_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean addFood(String name, String category, int carb, int prot, int fat,int kal, Bitmap img){
        boolean exist = findFood(name);
        if (exist == true) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        saveImage(img,name.toLowerCase());

        cv.put(CNAME_FOOD,name.toLowerCase());
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);
        cv.put(CKAL,kal);

        long result = db.insert(FOOD_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean deleteFood(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE + " WHERE food_name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.delete(FOOD_TABLE,"food_name=?", new String[]{name});
            if (result == -1) return false;
            deleteImage(name);
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean modifyFood(String food_name,String new_name,String category,Bitmap img,int carb, int prot, int fat, int kal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CNAME_FOOD,new_name);
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);
        cv.put(CKAL,kal);

        deleteImage(food_name);
        saveImage(img,new_name.toLowerCase());

        Cursor cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE + " WHERE food_name = ? ", new String[]{food_name});
        if (cursor.getCount() > 0) {
            long result = db.update(FOOD_TABLE, cv, "food_name=?", new String[]{food_name});
            if (result == -1) return false;
            return true;
        }
        else return false;
    }

    public Cursor readAllDataFood(){
        String  query = "SELECT * FROM "+ FOOD_TABLE + " ORDER BY "+CNAME_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readFilteredFoodByName(String name_prefix){
        String query = "SELECT * FROM "+FOOD_TABLE+" WHERE food_name LIKE '"+name_prefix+"%';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,new String[]{});
        }
        return cursor;
    }

    public Cursor readFilteredFoodByCategory(String filter){
        String query = "SELECT * FROM "+ FOOD_TABLE+" WHERE food_category=?" + " ORDER BY "+CNAME_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,new String[]{filter});
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

    public ArrayList<Integer> getFoodInfo(String name){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ArrayList<String> tmp = new ArrayList<String>();
        String query = "SELECT * FROM "+FOOD_TABLE+" WHERE food_name=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{name});
        while (cursor.moveToNext()){
            tmp.add(cursor.getString(2).toLowerCase());
            tmp.add(cursor.getString(3).toLowerCase());
            tmp.add(cursor.getString(4).toLowerCase());
            tmp.add(cursor.getString(5).toLowerCase());
        }
        if(tmp.size()>0) {
            ret.add(Integer.valueOf(tmp.get(0)));
            ret.add(Integer.valueOf(tmp.get(1)));
            ret.add(Integer.valueOf(tmp.get(2)));
            ret.add(Integer.valueOf(tmp.get(3)));
        }
        return ret;
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

    public Boolean addUser(String username,String password, int weight, int height, int sex, int carb_goal, int prot_goal, int fat_goal, int kal_goal, String question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Bitmap img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.no_image2);

        cv.put(CUSERNAME,username);
        cv.put(CPASSWORD,password);
        cv.put(CWEIGHT,weight);
        cv.put(CHEIGHT,height);
        cv.put(CSEX,sex);
        cv.put(CCARB_GOAL,carb_goal);
        cv.put(CPROT_GOAL,prot_goal);
        cv.put(CFAT_GOAL,fat_goal);
        cv.put(CKAL_GOAL,kal_goal);
        cv.put(QUESTION,question);

        long result = db.insert(USER_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean addUser(String username,String password,Bitmap img, int weight, int height, int sex, int carb_goal, int prot_goal, int fat_goal, int kal_goal, String question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        saveImage(img,username);

        cv.put(CUSERNAME,username);
        cv.put(CPASSWORD,password);
        cv.put(CWEIGHT,weight);
        cv.put(CHEIGHT,height);
        cv.put(CSEX,sex);
        cv.put(CCARB_GOAL,carb_goal);
        cv.put(CPROT_GOAL,prot_goal);
        cv.put(CFAT_GOAL,fat_goal);
        cv.put(CKAL_GOAL,kal_goal);
        cv.put(QUESTION,question);

        long result = db.insert(USER_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public String getUserAnswer(String username){
        String query = "SELECT "+QUESTION+" FROM "+USER_TABLE+" WHERE username=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return "";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if(cursor.getCount() == 0) return "";
        cursor.moveToNext();
        return cursor.getString(0);
    }

    public String getUserPassword(String username){
        String query = "SELECT "+CPASSWORD+" FROM "+USER_TABLE+" WHERE username=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return "";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if(cursor.getCount() == 0) return "";
        cursor.moveToNext();
        return cursor.getString(0);
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
        else return false;
    }

    public Boolean updateUserGoals(String username,int carb, int prot, int fat, int kal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CCARB_GOAL,carb);
        cv.put(CPROT_GOAL,prot);
        cv.put(CFAT_GOAL,fat);
        cv.put(CKAL_GOAL,kal);
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE username = ? ", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = db.update(USER_TABLE, cv, "username=?", new String[]{username});
            if (result == -1) return false;
            return true;
        }
        else return false;
    }

    public Boolean updateUserStat(String username,int weight, int height){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CWEIGHT,weight);
        cv.put(CHEIGHT,height);
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE username = ? ", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = db.update(USER_TABLE, cv, "username=?", new String[]{username});
            if (result == -1) return false;
            return true;
        }
        else return false;
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

    public Cursor getUserInfo(String username){
        String query = "SELECT * FROM "+USER_TABLE+" WHERE username=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) return null;
        Cursor cursor = db.rawQuery(query, new String[]{username});
        return cursor;
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
            res += cursor.getString(0)+" "+cursor.getString(1)+" "
                                            +cursor.getString(2)+" "
                                            +cursor.getString(3)+" "
                                            +cursor.getString(4)+" "
                                            +cursor.getString(5)+" "
                                            +cursor.getString(6)+" "
                                            +cursor.getString(7)+" "
                                            +cursor.getString(8)+"\n";
        }
        return res;
    }


    public void addExistingExercise(){
        Bitmap img_bitmap;
        byte[] img_bytes;

        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.no_image2);
        addExercise("Test","Chest",3,1,img_bitmap);

        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bench_press);
        addExercise("bench_press","Chest",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.incline_press);
        addExercise("incline_press","Chest",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.close_grip_press);
        addExercise("close_grip_press","Chest",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.decline_press);
        addExercise("decline_press","Chest",1,1,img_bitmap);

        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.front_raise);
        addExercise("front_raise","Shoulders",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.shoulder_press);
        addExercise("shoulder_press","Shoulders",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.shrug);
        addExercise("shrug","Shoulders",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.upright_row);
        addExercise("upright_row","Shoulders",1,1,img_bitmap);

        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.curl);
        addExercise("curl","Arms",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.reverse_curl);
        addExercise("reverse_curl","Arms",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.kickback);
        addExercise("kickback","Arms",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.tricep_extension);
        addExercise("tricep_extension","Arms",1,1,img_bitmap);

        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pullover);
        addExercise("pullover","Beck",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.row);
        addExercise("row","Beck",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.deadlift);
        addExercise("deadlift","Beck",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.sumo_deadlift);
        addExercise("sumo_deadlift","Beck",1,1,img_bitmap);

        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.squat);
        addExercise("squat","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.front_squat);
        addExercise("front_squat","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.hip_raise);
        addExercise("hip_raise","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.glute_bridge);
        addExercise("glute_bridge","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lunge);
        addExercise("lunge","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.good_morning);
        addExercise("good_morning","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.step_up);
        addExercise("step_up","Legs",1,1,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.calf_raise);
        addExercise("calf_raise","Legs",1,1,img_bitmap);
    }

    public void addExistingFood() {
        Bitmap img_bitmap;
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.tomatoes);
        addFood("Tomatoes","FruitVegetable",3,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bananas);
        addFood("Bananas","FruitVegetable",27,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.watermelons);
        addFood("Watermelons","FruitVegetable",11,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.apples);
        addFood("Apples","FruitVegetable",25,1,0,5,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.oranges);
        addFood("Oranges","FruitVegetable",16,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.mangoes);
        addFood("Mangoes","fruitvegetabeles",25,1,1,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pears);
        addFood("Pears","FruitVegetable",27,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.avocadoes);
        addFood("Avocadoes","FruitVegetable",8,2,15,5,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.grapes);
        addFood("Grapes","FruitVegetable",14,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pineapples);
        addFood("Pineapples","FruitVegetable",13,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.melons);
        addFood("Melons","FruitVegetable",8,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.peaches);
        addFood("Peaches","FruitVegetable",12,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lemons);
        addFood("Lemons","FruitVegetable",9,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.plums);
        addFood("Plums","FruitVegetable",7,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.strawberries);
        addFood("Strawberries","FruitVegetable",8,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.blueberry);
        addFood("Blueberry","FruitVegetable",14,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.blackberry);
        addFood("Blackberry","FruitVegetable",14,2,1,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.coconut);
        addFood("Coconut","FruitVegetable",10,3,27,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.kiwi);
        addFood("Kiwi","FruitVegetable",10,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pomegranate);
        addFood("Pomegranate","FruitVegetable",19,2,1,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cherry);
        addFood("Cherry","FruitVegetable",13,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cucumber);
        addFood("Cucumber","FruitVegetable",4,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.potatoes);
        addFood("potatoes","FruitVegetable",20,3,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.potatoes);
        addFood("onions","FruitVegetable",9,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.carrots);
        addFood("Carrots","FruitVegetable",9,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lettuce);
        addFood("lettuce","FruitVegetable",3,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bellpeppers);
        addFood("Bellpeppers","FruitVegetable",7,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cherry);
        addFood("Cherry","FruitVegetable",13,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.salad);
        addFood("salad","FruitVegetable",4,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.mushrooms);
        addFood("mushrooms","FruitVegetable",6,3,1,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.corn);
        addFood("corn","FruitVegetable",22,3,2,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.spinach);
        addFood("spinach","FruitVegetable",4,3,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.greenbeans);
        addFood("greenbeans","FruitVegetable",9,2,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cabbage);
        addFood("cabbage","FruitVegetable",4,1,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.broccoli);
        addFood("broccoli","FruitVegetable",6,3,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pork);
        addFood("pork","MeatFisheggs",0,29,16,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.beef);
        addFood("beef","MeatFisheggs",0,29,18,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lamb);
        addFood("Lamb","MeatFisheggs",0,28,24,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.chicken);
        addFood("Chicken","MeatFisheggs",0,26,14,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.turkey);
        addFood("Turkey","MeatFisheggs",0,32,8,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.duck);
        addFood("Duck","MeatFisheggs",0,19,28,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.sausage);
        addFood("Sausage","MeatFisheggs",1,12,28,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.wrustel);
        addFood("wrustel","MeatFisheggs",0,11,28,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.butter);
        addFood("Butter","Dairy",0,1,84,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.chees);
        addFood("cheese","Dairy",3,25,37,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.yogurt);
        addFood("yogurt","Dairy",6,5,2,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.milk);
        addFood("Milk","Dairy",6,4,2,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bluecheese);
        addFood("bluecheese","Dairy",2,24,32,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cheddar);
        addFood("cheddar","Dairy",3,26,37,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.mozzarella);
        addFood("mozzarella","Dairy",2,25,25,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.parmesan);
        addFood("parmesan","Dairy",14,28,28,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bread);
        addFood("bread","Cereal",48,13,2,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.kellogs);
        addFood("cornflakes","Cereal",73,19,2,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pasta);
        addFood("pasta","Cereal",80,7,2,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.spaghetti);
        addFood("spaghetti","Cereal",74,13,1,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.rice);
        addFood("rice","Cereal",28,2,0,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.ricecake);
        addFood("ricecake","Cereal",81,8,3,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.corn);
        addFood("corn","Cereal",74,10,5,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.rusk);
        addFood("rusk","Cereal",72,13,7,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.biscuits1);
        addFood("biscuit1","Cereal",70,7,10,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.biscuits2);
        addFood("cookie","Cereal",60,5,27,0,img_bitmap);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.oreo);
        addFood("oreo","Cereal",68,5,19,0,img_bitmap);

    }

}
