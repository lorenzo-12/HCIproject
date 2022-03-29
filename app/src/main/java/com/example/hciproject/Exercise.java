package com.example.hciproject;

public class Exercise {

    public String name;
    public String category;


    public Exercise(String name,String category){
        this.name = name.toLowerCase();
        this.category = category.toLowerCase();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())){
            return false;
        }
        Exercise other = (Exercise) obj;
        if (other.name.equals(this.name)){
            return true;
        }
        return false;
    }

    public void Exercise_ModifyName(String new_name){
        this.name = new_name.toLowerCase();
    }

    public void Exercise_ModifyCategory(String new_category){
        this.category = new_category.toLowerCase();
    }

    public void print(){
        System.out.println("\t    \\___"+this.name+","+this.category);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode()+this.category.hashCode();
    }

    @Override
    public String toString() {
        return "E;"+this.name+";"+this.category+"\n";
    }

    public Exercise(String exercise_string){
        if ((exercise_string == null) || (exercise_string.equals(""))){
            return;
        }
        exercise_string = exercise_string.replace("\n","");
        String[] param = exercise_string.split(";");

        if ((param.length == 3) && (param[0].equals("E"))){
            this.name = param[1].toLowerCase();
            this.category = param[2].toLowerCase();
        }
    }

}
