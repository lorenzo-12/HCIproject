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

    public static final String CREATE_USER_TABLE = "CREATE TABLE "+USER_TABLE+" ("+CUSERNAME+" TEXT PRIMARY KEY, "+CPASSWORD+" TEXT NOT NULL);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME);
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

    public Cursor readAllData(){
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
        Cursor cursor = readAllData();
        if(cursor.getCount() == 0) return "No Data";
        String res = "";
        while (cursor.moveToNext()){
            res += cursor.getString(0)+" "+cursor.getString(1)+"\n";
        }
        return res;
    }

}
