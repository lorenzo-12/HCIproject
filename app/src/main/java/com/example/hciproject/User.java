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

    public void setUsername(String new_usename){
        this.username = new_usename.toLowerCase();
    }

    public void setPassword(String new_password){
        this.username = new_password.toLowerCase();
    }

    public void addDailyDiet(DailyDiet dailydiet){
        this.diet.add(dailydiet);
    }

    public void print(){
        System.out.println("Username: "+this.username);
        System.out.println("Password: "+this.password);
        for (DailyDiet dd : this.diet){
            dd.print();
        }
    }

    //siccome ho modificato EQUALS devo modificare anche HASHCODE
    @Override
    public int hashCode(){
        return this.username.hashCode()+this.password.hashCode()+this.diet.hashCode();
    }


    @Override
    public String toString() {
        String res = "U;"+this.username+";"+this.password+"\n";
        for (DailyDiet d : this.diet){
            res = res+d.toString()+"<end_user>\n";
        }
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
        if ((lines.length == 0) || (lines[0].length() == 0)){
            return;
        }
        String prima_riga_strig = lines[0].replace("\n", "");
        String[] prima_riga = prima_riga_strig.split(";");

        if ((prima_riga.length != 3) || (prima_riga[0].equals("U") == false)) return;
        this.username = prima_riga[1].toLowerCase();
        this.password = prima_riga[2].toLowerCase();

        String lista_dd = user_string.replace(prima_riga_strig, "");
        String[] DD = lista_dd.split("<end_dailydiet>\n");

        for (String e : DD){
            e = e.trim()+"\n";
            DailyDiet d = new DailyDiet(e);
            this.diet.add(d);
        }

    }

}