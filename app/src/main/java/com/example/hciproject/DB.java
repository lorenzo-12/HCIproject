package com.example.hciproject;

import java.util.ArrayList;

public class DB {
    public ArrayList<User> users_list;

    public DB(){
        this.users_list=new ArrayList<User>();
    }

    public DB(ArrayList<User> l){
        this.users_list=l;
    }

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

}
