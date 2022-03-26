package com.example.hciproject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String username;
    public String password;
    //public Diet diet;

    public User(String u, String p){
        this.username=u;
        this.password=p;
        //this.diet=new Diet();
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
    }
    /*
    public User(String u,String p, Diet d){
        this.username=u;
        this.password=p;
        this.diet=d;
    }

    public void changeDiet(Diet d){
        this.diet=d;
    }
    */

}