package com.example.hciproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterDiaryFood extends RecyclerView.Adapter<CustomAdapterDiaryFood.MyViewHolder> {

    public OnItemClickListener mlistener;
    Context context;
    ArrayList<String> food_name_list, food_category_list, food_carb_list, food_prot_list, food_fat_list,food_kal_list;
    ArrayList<Bitmap> food_img_list;
    DBHelper db;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt,category_txt,carb_txt,prot_txt,fat_txt,kal_txt;
        public ImageView mdeletebtn,mupdatebtn,food_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.food_name);
            category_txt = itemView.findViewById(R.id.food_category);
            carb_txt = itemView.findViewById(R.id.food_carb);
            prot_txt = itemView.findViewById(R.id.food_prot);
            fat_txt = itemView.findViewById(R.id.food_fat);
            kal_txt = itemView.findViewById(R.id.food_kal);
            mdeletebtn = itemView.findViewById(R.id.delete_food_img);
            mupdatebtn = itemView.findViewById(R.id.modify_food_img);
            food_img = itemView.findViewById(R.id.food_img);
            db = new DBHelper(context);

            mupdatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlistener.onUpdateClick(position);
                        }
                    }
                }
            });

            mdeletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlistener.onDeleteClick(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public CustomAdapterDiaryFood.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_food,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterDiaryFood.MyViewHolder holder, int position) {
        holder.name_txt.setText(String.valueOf(food_name_list.get(position)));
        holder.category_txt.setText(String.valueOf(food_category_list.get(position)));
        holder.carb_txt.setText(String.valueOf(food_carb_list.get(position)));
        holder.prot_txt.setText(String.valueOf(food_prot_list.get(position)));
        holder.fat_txt.setText(String.valueOf(food_fat_list.get(position)));
        holder.kal_txt.setText(String.valueOf(food_kal_list.get(position)));
        Bitmap b = db.loadImage(String.valueOf(food_name_list.get(position)));
        holder.food_img.setImageBitmap(b);

    }

    @Override
    public int getItemCount() {
        return food_name_list.size();
    }

    CustomAdapterDiaryFood(Context context, ArrayList list_name, ArrayList list_category, ArrayList list_carb, ArrayList list_prot, ArrayList list_fat, ArrayList list_kal, ArrayList list_img ){
        this.context = context;
        this.food_name_list = list_name;
        this.food_category_list = list_category;
        this.food_carb_list = list_carb;
        this.food_prot_list = list_prot;
        this.food_fat_list = list_fat;
        this.food_kal_list = list_kal;
        this.food_img_list = list_img;
    }

}
