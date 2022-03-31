package com.example.hciproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    public static final String CNAME_FOOD = "food_name";
    public static final String CCATEGORY_FOOD = "food_category";
    public static final String CCARB = "carb";
    public static final String CPROT = "prot";
    public static final String CFAT = "fat";

    Context context;
    ArrayList<String> food_name_list, food_category_list, food_carb_list, food_prot_list, food_fat_list;


    CustomAdapter(Context context, ArrayList list_name,ArrayList list_category,ArrayList list_carb,ArrayList list_prot,ArrayList list_fat ){
        this.context = context;
        this.food_name_list = list_name;
        this.food_category_list = list_category;
        this.food_carb_list = list_carb;
        this.food_prot_list = list_prot;
        this.food_fat_list = list_fat;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.name_txt.setText(String.valueOf(food_name_list.get(position)));
        holder.category_txt.setText(String.valueOf(food_category_list.get(position)));
        holder.carb_txt.setText(String.valueOf(food_carb_list.get(position)));
        holder.prot_txt.setText(String.valueOf(food_prot_list.get(position)));
        holder.fat_txt.setText(String.valueOf(food_fat_list.get(position)));

    }

    @Override
    public int getItemCount() {
        return food_name_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt,category_txt,carb_txt,prot_txt,fat_txt;
        public ImageView deletebtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.food_name);
            category_txt = itemView.findViewById(R.id.food_category);
            carb_txt = itemView.findViewById(R.id.food_carb);
            prot_txt = itemView.findViewById(R.id.food_prot);
            fat_txt = itemView.findViewById(R.id.food_fat);
            deletebtn = itemView.findViewById(R.id.delete_food_btn);

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }
}
