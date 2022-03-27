package com.example.hciproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//stessa logica della classe Userr, siccome devo passare variabili tra le attività
//devo fare si che la classa implementi PARCELABLE
public class DB implements Parcelable {
    public String User_logged;
    public ArrayList<User> users_list;

    //costruttore della classe
    public DB(){
        this.User_logged = "none";
        this.users_list=new ArrayList<User>();
    }

    public DB(ArrayList<User> l){
        this.User_logged = "none";
        this.users_list=l;
    }

    protected DB(Parcel in) {
        User_logged = in.readString();
        users_list = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<DB> CREATOR = new Creator<DB>() {
        @Override
        public DB createFromParcel(Parcel in) {
            return new DB(in);
        }

        @Override
        public DB[] newArray(int size) {
            return new DB[size];
        }
    };

    public void addUser(User u){
        for(User e : this.users_list){
            if (e.username.equals(u.username)){
                return;
            }
        }
        this.users_list.add(u);
    }

    public void removeUser(User u){
        this.users_list.remove(u);
    }

    public void setUser(User u){
        if (this.users_list.contains(u)){
            this.User_logged=u.username;
        }
    }

    @Override
    public String toString() {
        String res = "UL;"+this.User_logged+"\n";
        for (User u : this.users_list){
            res = res+u.toString();
        }
        return res;
    }

    public DB(String db_string){
        this.User_logged = "none";
        this.users_list = new ArrayList<User>();

        if ((db_string == null) || (db_string.equals(""))){
            return;
        }
        String[] lines = db_string.split("\n");
        for (String line : lines){
            line = line.replace("\n","");
            if (line.length()==0) return;
            String[] param = line.split(";");
            if ((param.length == 2) && (param[0].equals("UL"))){
                this.User_logged = param[1];
            }
            else if ((param.length == 3) && (param[0].equals("U"))){
                User u = new User(line);
                this.addUser(u);
            }
        }

    }

    public void unsetUser(){
        this.User_logged = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(User_logged);
        parcel.writeTypedList(users_list);
    }
}
