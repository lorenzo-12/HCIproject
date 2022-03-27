package com.example.hciproject;

import java.util.ArrayList;

//stessa logica della classe Userr, siccome devo passare variabili tra le attività
//devo fare si che la classa implementi PARCELABLE
public class DB {
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

    public boolean containUser(User u){
        if (this.users_list.contains(u)) return true;
        return false;
    }

    public boolean containUser(String username){
        for (User e : this.users_list){
            if (e.username.equals(username)) return true;
        }
        return false;
    }

    public User getUser(String username){
        for (User e : this.users_list){
            if (e.username.equals(username)) return e;
        }
        return null;
    }

    public User getUser(User u){
        if (containUser(u) == true){
            for (User e : this.users_list){
                if (e.equals(u)) return u;
            }
        }
        return null;
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

}
