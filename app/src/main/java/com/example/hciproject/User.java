package com.example.hciproject;

import android.os.Parcel;
import android.os.Parcelable;


//siccome il mio intento è quello di passare non delle semplici stringhe tra le attività
//ma di passare delle variabili, allora devo far si che la classe implementi PARCELABLE
public class User implements Parcelable {

    //un utente è costituito dalle info di base
    public String username;
    public String password;
    //e dalla dieta che sta facendo
    //dove la dieta non è altro che una lista di <lista_cibi_mangiati_il_giotno_X, giorno x>
    //siccome non voglio immettere subito 300 cose nel sistema per ora considero il cibo
    //public Diet diet;

    //funzione per aggiungere un utente
    public User(String u, String p){
        this.username = u.toLowerCase();
        this.password = p.toLowerCase();
        //this.diet=new Diet();
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
        return this.username.hashCode()+this.password.hashCode();
    }


    @Override
    public String toString() {
        return "U;"+this.username+";"+this.password+"\n";
    }

    public User(String user_string){
        this.username = "";
        this.password = "";
        if ((user_string == null) || (user_string.equals(""))){
            return;
        }
        user_string = user_string.replace("\n","");
        String[] param = user_string.split(";");
        if ((param.length == 3) && (param[0].equals("U"))){
            this.username = param[1].toLowerCase();
            this.password = param[2].toLowerCase();
        }
    }

    //tutta sta roba l'ha creata il sistema automaticamente poichè implementa PARCEL
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

    //funzioni relative alla dieta, ma che siccome non stiamo considerando ora le ho commentate in modo da non
    //avere errori nel codice
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