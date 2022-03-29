package com.example.hciproject;

public class Food {

    public String name;
    public int kcal;
    public String category;
    public int quantity;

    public Food(String food_name, int food_kcal){
        this.name = food_name.toLowerCase();
        this.kcal = food_kcal;
        this.category = "none".toLowerCase();
        this.quantity = 1;
    }

    public Food(String food_name, int food_kcal,int food_quantity){
        this.name = food_name.toLowerCase();
        this.kcal = food_kcal;
        this.category = "none".toLowerCase();
        this.quantity = food_quantity;
    }

    public Food(String food_name, int food_kcal,String food_category,int food_quantity){
        this.name = food_name.toLowerCase();
        this.kcal = food_kcal;
        this.category = food_category.toLowerCase();
        this.quantity = food_quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())){
            return false;
        }
        Food other = (Food) obj;
        if (other.name.equals(this.name)) {
            return true;
        }
        return false;
    }

    public void setCategory(String new_category){
        this.category = new_category.toLowerCase();
    }

    public void setKcal(int new_kcal){
        this.kcal = new_kcal;
    }

    public void setName(String new_name){
        this.name = new_name.toLowerCase();
    }

    public void setQuantity(int new_quantity){
        this.quantity = new_quantity;
    }

    public void print(){
        System.out.println("\t    \\___"+this.name+","+this.kcal+","+this.category+","+this.quantity);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode()+this.kcal+this.category.hashCode()+this.quantity;
    }

    @Override
    public String toString() {
        return "F;"+this.name+";"+this.kcal+";"+this.category+";"+this.quantity+"\n";
    }

    public Food(String food_string){
        if ((food_string == null) || (food_string.equals(""))){
            return;
        }
        food_string = food_string.replace("\n","");
        String[] param = food_string.split(";");

        if ((param.length == 5) && (param[0].equals("F"))){

            int num_kcal,num_quantity;
            try {
                num_kcal = Integer.parseInt(param[2]);
                num_quantity = Integer.parseInt(param[4]);
            }
            catch (NumberFormatException e) {
                num_kcal = 0;
                num_quantity = 1;
            }

            this.name = param[1].toLowerCase();
            this.kcal = num_kcal;
            this.category = param[3].toLowerCase();
            this.quantity = num_quantity;
        }
    }

}
