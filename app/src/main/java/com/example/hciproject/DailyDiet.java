package com.example.hciproject;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;

public class DailyDiet {

    public LocalDate date;
    public ArrayList<Food> food_list;
    public ArrayList<Exercise> exercise_list;

    public DailyDiet(LocalDate date){
        this.date = date;
        this.food_list = new ArrayList<Food>();
        this.exercise_list = new ArrayList<Exercise>();
    }

    public DailyDiet(LocalDate date,ArrayList<Food> food_list,ArrayList<Exercise> exercise_list){
        this.date = date;
        this.food_list = food_list;
        this.exercise_list = exercise_list;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public void setFoodList(ArrayList<Food> food_list){
        this.food_list = food_list;
    }

    public void setExerciseList(ArrayList<Exercise> exercise_list){
        this.exercise_list = exercise_list;
    }

    public void addFood(Food food){
        if (this.food_list.contains(food) == false){
            this.food_list.add(food);
        }
    }

    public void addExercise(Exercise exercise){
        if (this.exercise_list.contains(exercise) == false){
            this.exercise_list.add(exercise);
        }
    }

    public void removeFood(Food food){
        if (this.food_list.contains(food) == true){
            this.food_list.remove(food);
        }
    }

    public void removeExercise(Exercise exercise){
        if (this.exercise_list.contains(exercise) == true){
            this.exercise_list.remove(exercise);
        }
    }

    public Food findFood(String name){
        for (Food e : this.food_list){
            if (e.name.equals(name)) return e;
        }
        return null;
    }

    public Exercise findExercise(String name){
        for (Exercise e : this.exercise_list){
            if (e.name.equals(name)) return e;
        }
        return null;
    }

    public Boolean containsFood(Food food){
        return this.food_list.contains(food);
    }

    public Boolean containsExercise(Exercise exercise){
        return this.exercise_list.contains(exercise);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())){
            return false;
        }

        DailyDiet other = (DailyDiet) obj;
        if (this.date.equals(other.date) == false) return false;
        for (Food e : other.food_list){
            if (this.food_list.contains(e) == false) return false;
        }
        for (Food e : this.food_list){
            if (other.food_list.contains(e) == false) return false;
        }
        for (Exercise e : other.exercise_list){
            if (this.exercise_list.contains(e) == false) return false;
        }
        for (Exercise e : this.exercise_list){
            if (other.exercise_list.contains(e) == false) return false;
        }
        return true;
    }

    public void print(){
        System.out.println("\t\\___"+this.date);
        for (Food e : this.food_list){
            e.print();
        }
        for (Exercise e : this.exercise_list){
            e.print();
        }
    }

    @Override
    public int hashCode() {
        return this.date.hashCode()+this.food_list.hashCode()+this.food_list.hashCode();
    }

    @Override
    public String toString() {
        String res = "DD;"+this.date.toString()+"\n";

        for (Food e : this.food_list){
            res = res+"F;"+e.name+";"+e.kcal+";"+e.category+";"+e.quantity+"\n";
        }
        for (Exercise e : this.exercise_list){
            res = res+"E;"+e.name+";"+e.category+"\n";
        }
        res = res+"<end_dailydiet>\n";

        return res;
    }

    public DailyDiet(String dailydiet_string){

        this.date = LocalDate.now();
        this.food_list = new ArrayList<Food>();
        this.exercise_list = new ArrayList<Exercise>();

        if ((dailydiet_string == null) || (dailydiet_string.equals(""))) return;

        String[] lines = dailydiet_string.split("\n");
        if (lines.length == 0) return;
        String[] prima_riga = lines[0].replace("\n","").split(";");
        if ((prima_riga.length == 0)) return;
        if ((prima_riga[0].equals("DD") == false) || (prima_riga.length != 2)) return;
        try {
            this.date = LocalDate.parse(prima_riga[1]);
        } catch (Exception e){
            this.date = LocalDate.now();
        }
        for (String e : lines){
            String[] param = e.replace("\n","").split(";");
            if ((param.length == 5) && (param[0].equals("F"))){
                Food fo = new Food(e);
                this.food_list.add(fo);
            }
            else if ((param.length == 3) && (param[0].equals("E"))){
                Exercise ex = new Exercise(e);
                this.exercise_list.add(ex);
            }
        }
    }

}
