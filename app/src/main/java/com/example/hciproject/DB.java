package com.example.hciproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DB implements Parcelable {
    public ArrayList<User> users_list;

    public DB(){
        this.users_list=new ArrayList<User>();
    }

    public DB(ArrayList<User> l){
        this.users_list=l;
    }

    protected DB(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(users_list);
    }
}
