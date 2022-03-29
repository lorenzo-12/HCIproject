package com.example.hciproject;


import java.util.ArrayList;

//siccome il mio intento è quello di passare non delle semplici stringhe tra le attività
//ma di passare delle variabili, allora devo far si che la classe implementi PARCELABLE
public class User {

    //un utente è costituito dalle info di base
    public String username;
    public String password;
    public ArrayList<DailyDiet> diet;


    //e dalla dieta che sta facendo
    //dove la dieta non è altro che una lista di <lista_cibi_mangiati_il_giotno_X, giorno x>
    //siccome non voglio immettere subito 300 cose nel sistema per ora considero il cibo
    //public Diet diet;

    //funzione per aggiungere un utente
    public User(String u, String p){
        this.username = u.toLowerCase();
        this.password = p.toLowerCase();
        this.diet=new ArrayList<DailyDiet>();
    }

    //siccome dovrò fare dei controlli sugli utenti nella gestione del database modifico nella classe
    //stessa il concetto di uguaglianza tra utenti
    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj.getClass() != this.getClass()){
            return false;
        }

        User other = (User) obj;
        if (this.username.equals(other.username) && this.password.equals(other.password)){
            return true;
        }
        return false;
    }

    //siccome ho modificato EQUALS devo modificare anche HASHCODE
    @Override
    public int hashCode(){
        return this.username.hashCode()+this.password.hashCode()+this.diet.hashCode();
    }


    @Override
    public String toString() {
        String res = "U;"+this.username+";"+this.password+"\n";
        if (this.diet.isEmpty()) return res;
        res = res+this.diet.toString();
        return res;
    }

    public User(String user_string){
        this.username = "";
        this.password = "";
        this.diet = new ArrayList<DailyDiet>();
        if ((user_string == null) || (user_string.equals(""))){
            return;
        }

        String[] lines = user_string.split("\n");
        if (lines.length == 0) return;
        String[] prima_linea = lines[0].replace("\n","").split(";");
        if ((prima_linea.length != 3) || (prima_linea[0].equals("U") == false)) return;
        this.username = prima_linea[1].toLowerCase();
        this.password = prima_linea[2].toLowerCase();



    }

    public void setUsername(String new_usename){
        this.username = new_usename.toLowerCase();
    }

    public void setPassword(String new_password){
        this.username = new_password.toLowerCase();
    }

}
