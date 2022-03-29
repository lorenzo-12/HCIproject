package com.example.hciproject;

import java.util.ArrayList;

//stessa logica della classe Userr, siccome devo passare variabili tra le attività
//devo fare si che la classa implementi PARCELABLE
public class DB {
    public String User_logged;
    public ArrayList<User> users_list;

    //costruttore della classe
    public DB(){
        this.User_logged = "none".toLowerCase();
        this.users_list=new ArrayList<User>();
    }

    public DB(ArrayList<User> l){
        this.User_logged = "none".toLowerCase();
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
            this.User_logged = u.username.toLowerCase();
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

    public void unsetUser(){
        this.User_logged = "";
    }

    public void print(){
        System.out.println("Logged: "+this.User_logged);
        for (User e : this.users_list){
            e.print();
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

        db_string = db_string.trim();
        if ((db_string == null) || (db_string.equals(""))){
            return;
        }

        String[] lines = db_string.split("\n");
        String prima_riga_String = lines[0].replace("\n", "");
        String[] prima_riga = prima_riga_String.split(";");
        if ((lines.length == 0) || (prima_riga_String.length() == 0)) return;
        if ((prima_riga.length != 2) || (prima_riga[0].equals("DB") == false)) return;
        this.User_logged = prima_riga[1].toLowerCase();

        String lista_utenti_stringa = db_string.replace(prima_riga_String, "");
        String[] lista_utenti = lista_utenti_stringa.split("<end_user>\n");

        for (String e : lista_utenti){
            e = e.trim()+"\n";
            User u = new User(e);
            this.users_list.add(u);
        }

    }

}
