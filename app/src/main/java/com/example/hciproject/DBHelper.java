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

public class DBHelper extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "mydatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String CUSERNAME = "username";
    public static final String CPASSWORD = "password";
    public static final String CIMG_USER = "user_img";

    public static final String FOOD_TABLE = "food";
    public static final String CNAME_FOOD = "food_name";
    public static final String CCATEGORY_FOOD = "food_category";
    public static final String CCARB = "carb";
    public static final String CPROT = "prot";
    public static final String CFAT = "fat";
    public static final String CIMG_FOOD = "food_img";

    public static final String EXERCISE_TABLE = "exercise";
    public static final String CNAME_EXERCISE = "exercise_name";
    public static final String CCATEGORY_EXERCISE = "exercise_category";
    public static final String CREPS_EXERCISE = "exercise_reps";
    public static final String CSERIES_EXERCISE = "exercise_series";

    public static final String CREATE_USER_TABLE = "CREATE TABLE "+USER_TABLE+" ("+CUSERNAME+" TEXT PRIMARY KEY, "+CPASSWORD+" TEXT NOT NULL, "+CIMG_USER+" BLOB );";
    public static final String CREATE_FOOD_TABLE = "CREATE TABLE "+FOOD_TABLE+" ("+CNAME_FOOD+" TEXT PRIMARY KEY, "+CCATEGORY_FOOD+" TEXT, "+CCARB+" INTEGER, "+CPROT+" INTEGER, "+CFAT+" INTEGER, "+CIMG_FOOD+" BLOB );";
    public static final String CREATE_WORKOUT_TABLE = "CREATE TABLE "+ EXERCISE_TABLE +" ("+ CNAME_EXERCISE +" TEXT PRIMARY KEY, "+ CCATEGORY_EXERCISE +" TEXT, "+ CREPS_EXERCISE +" INTEGER, "+ CSERIES_EXERCISE +" INTEGER);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

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
        db.execSQL(CREATE_WORKOUT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ FOOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ EXERCISE_TABLE);
    }

    public Boolean addWorkout(String name,String category,int reps, int series){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CNAME_EXERCISE,name);
        cv.put(CCATEGORY_EXERCISE,category);
        cv.put(CREPS_EXERCISE,reps);
        cv.put(CSERIES_EXERCISE,series);

        long result = db.insert(EXERCISE_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }

    public Boolean updateWorkout(String name,String category,int reps,int series){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CCATEGORY_EXERCISE,category);
        cv.put(CREPS_EXERCISE,reps);
        cv.put(CSERIES_EXERCISE,series);
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXERCISE_TABLE + " WHERE exercise_name = ? ", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.update(EXERCISE_TABLE, cv, "exercise_name=?", new String[]{name});
            if (result == -1) return false;
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean deleteWorkout(String name){
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
        String  query = "SELECT * FROM "+ EXERCISE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readFilteredExercise(String filter){
        String query = "SELECT * FROM "+ EXERCISE_TABLE +" WHERE exercise_category=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,new String[]{filter});
        }
        return cursor;
    }

    public Boolean findWorkout(String name){
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

    public Boolean addFood(String name, String category, int carb, int prot, int fat){
        boolean exist = findFood(name);
        if (exist == true) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Bitmap img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.apples);
        byte[] img_bytes = getBytes(img_bitmap);

        cv.put(CNAME_FOOD,name.toLowerCase());
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);
        cv.put(CIMG_FOOD,img_bytes);

        long result = db.insert(FOOD_TABLE,null,cv);
        if (result == -1) return false;
        return true;
    }
    public Boolean addFood(String name, String category, int carb, int prot, int fat, byte[] img){
        boolean exist = findFood(name);
        if (exist == true) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CNAME_FOOD,name.toLowerCase());
        cv.put(CCATEGORY_FOOD,category);
        cv.put(CCARB,carb);
        cv.put(CPROT,prot);
        cv.put(CFAT,fat);
        cv.put(CIMG_FOOD,img);

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
        String  query = "SELECT * FROM "+ FOOD_TABLE + " ORDER BY "+CNAME_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readFilteredFood(String filter){
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

        Bitmap img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.apples);
        byte[] img_bytes = getBytes(img_bitmap);

        cv.put(CUSERNAME,username);
        cv.put(CPASSWORD,password);
        cv.put(CIMG_USER, img_bytes);

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

    //funzione per inserire i cibi di base.
    //private poichè deve essere chiamata solamente alla creazione del database
    public void addExistingFood() {
        Bitmap img_bitmap;
        byte[] img_bytes;


        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.tomatoes);
        img_bytes = getBytes(img_bitmap);
        addFood("Tomatoes","FruitVegetable",3,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bananas);
        img_bytes = getBytes(img_bitmap);
        addFood("Bananas","FruitVegetable",27,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.watermelons);
        img_bytes = getBytes(img_bitmap);
        addFood("Watermelons","FruitVegetable",11,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.apples);
        img_bytes = getBytes(img_bitmap);
        addFood("Apples","FruitVegetable",25,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.oranges);
        img_bytes = getBytes(img_bitmap);
        addFood("Oranges","FruitVegetable",16,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.mangoes);
        img_bytes = getBytes(img_bitmap);
        addFood("Mangoes","fruitvegetabeles",25,1,1,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pears);
        img_bytes = getBytes(img_bitmap);
        addFood("Pears","FruitVegetable",27,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.avocadoes);
        img_bytes = getBytes(img_bitmap);
        addFood("Avocadoes","FruitVegetable",8,2,15,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.grapes);
        img_bytes = getBytes(img_bitmap);
        addFood("Grapes","FruitVegetable",14,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pineapples);
        img_bytes = getBytes(img_bitmap);
        addFood("Pineapples","FruitVegetable",13,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.melons);
        img_bytes = getBytes(img_bitmap);
        addFood("Melons","FruitVegetable",8,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.peaches);
        img_bytes = getBytes(img_bitmap);
        addFood("Peaches","FruitVegetable",12,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lemons);
        img_bytes = getBytes(img_bitmap);
        addFood("Lemons","FruitVegetable",9,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.plums);
        img_bytes = getBytes(img_bitmap);
        addFood("Plums","FruitVegetable",7,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.strawberries);
        img_bytes = getBytes(img_bitmap);
        addFood("Strawberries","FruitVegetable",8,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.blueberry);
        img_bytes = getBytes(img_bitmap);
        addFood("Blueberry","FruitVegetable",14,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.blackberry);
        img_bytes = getBytes(img_bitmap);
        addFood("Blackberry","FruitVegetable",14,2,1,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.coconut);
        img_bytes = getBytes(img_bitmap);
        addFood("Coconut","FruitVegetable",10,3,27,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.kiwi);
        img_bytes = getBytes(img_bitmap);
        addFood("Kiwi","FruitVegetable",10,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pomegranate);
        img_bytes = getBytes(img_bitmap);
        addFood("Pomegranate","FruitVegetable",19,2,1,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cherry);
        img_bytes = getBytes(img_bitmap);
        addFood("Cherry","FruitVegetable",13,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cucumber);
        img_bytes = getBytes(img_bitmap);
        addFood("Cucumber","FruitVegetable",4,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.potatoes);
        img_bytes = getBytes(img_bitmap);
        addFood("potatoes","FruitVegetable",20,3,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.potatoes);
        img_bytes = getBytes(img_bitmap);
        addFood("onions","FruitVegetable",9,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.carrots);
        img_bytes = getBytes(img_bitmap);
        addFood("Carrots","FruitVegetable",9,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lettuce);
        img_bytes = getBytes(img_bitmap);
        addFood("lettuce","FruitVegetable",3,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bellpeppers);
        img_bytes = getBytes(img_bitmap);
        addFood("Bellpeppers","FruitVegetable",7,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cherry);
        img_bytes = getBytes(img_bitmap);
        addFood("Cherry","FruitVegetable",13,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.salad);
        img_bytes = getBytes(img_bitmap);
        addFood("salad","FruitVegetable",4,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.mushrooms);
        img_bytes = getBytes(img_bitmap);
        addFood("mushrooms","FruitVegetable",6,3,1,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.corn);
        img_bytes = getBytes(img_bitmap);
        addFood("corn","FruitVegetable",22,3,2,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.spinach);
        img_bytes = getBytes(img_bitmap);
        addFood("spinach","FruitVegetable",4,3,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.greenbeans);
        img_bytes = getBytes(img_bitmap);
        addFood("greenbeans","FruitVegetable",9,2,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cabbage);
        img_bytes = getBytes(img_bitmap);
        addFood("cabbage","FruitVegetable",4,1,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.broccoli);
        img_bytes = getBytes(img_bitmap);
        addFood("broccoli","FruitVegetable",6,3,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pork);
        img_bytes = getBytes(img_bitmap);
        addFood("pork","MeatFisheggs",0,29,16,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.beef);
        img_bytes = getBytes(img_bitmap);
        addFood("beef","MeatFisheggs",0,29,18,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.lamb);
        img_bytes = getBytes(img_bitmap);
        addFood("Lamb","MeatFisheggs",0,28,24,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.chicken);
        img_bytes = getBytes(img_bitmap);
        addFood("Chicken","MeatFisheggs",0,26,14,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.turkey);
        img_bytes = getBytes(img_bitmap);
        addFood("Turkey","MeatFisheggs",0,32,8,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.duck);
        img_bytes = getBytes(img_bitmap);
        addFood("Duck","MeatFisheggs",0,19,28,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.sausage);
        img_bytes = getBytes(img_bitmap);
        addFood("Sausage","MeatFisheggs",1,12,28,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.wrustel);
        img_bytes = getBytes(img_bitmap);
        addFood("wrustel","MeatFisheggs",0,11,28,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.butter);
        img_bytes = getBytes(img_bitmap);
        addFood("Butter","Dairy",0,1,84,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.chees);
        img_bytes = getBytes(img_bitmap);
        addFood("cheese","Dairy",3,25,37,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.yogurt);
        img_bytes = getBytes(img_bitmap);
        addFood("yogurt","Dairy",6,5,2,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.milk);
        img_bytes = getBytes(img_bitmap);
        addFood("Milk","Dairy",6,4,2,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bluecheese);
        img_bytes = getBytes(img_bitmap);
        addFood("bluecheese","Dairy",2,24,32,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.cheddar);
        img_bytes = getBytes(img_bitmap);
        addFood("cheddar","Dairy",3,26,37,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.mozzarella);
        img_bytes = getBytes(img_bitmap);
        addFood("mozzarella","Dairy",2,25,25,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.parmesan);
        img_bytes = getBytes(img_bitmap);
        addFood("parmesan","Dairy",14,28,28,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.bread);
        img_bytes = getBytes(img_bitmap);
        addFood("bread","Cereal",48,13,2,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.kellogs);
        img_bytes = getBytes(img_bitmap);
        addFood("cornflakes","Cereal",73,19,2,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.pasta);
        img_bytes = getBytes(img_bitmap);
        addFood("pasta","Cereal",80,7,2,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.spaghetti);
        img_bytes = getBytes(img_bitmap);
        addFood("spaghetti","Cereal",74,13,1,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.rice);
        img_bytes = getBytes(img_bitmap);
        addFood("rice","Cereal",28,2,0,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.ricecake);
        img_bytes = getBytes(img_bitmap);
        addFood("ricecake","Cereal",81,8,3,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.corn);
        img_bytes = getBytes(img_bitmap);
        addFood("corn","Cereal",74,10,5,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.rusk);
        img_bytes = getBytes(img_bitmap);
        addFood("rusk","Cereal",72,13,7,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.biscuits1);
        img_bytes = getBytes(img_bitmap);
        addFood("biscuit1","Cereal",70,7,10,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.biscuits2);
        img_bytes = getBytes(img_bitmap);
        addFood("cookie","Cereal",60,5,27,img_bytes);
        img_bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.oreo);
        img_bytes = getBytes(img_bitmap);
        addFood("oreo","Cereal",68,5,19,img_bytes);
    }

}
