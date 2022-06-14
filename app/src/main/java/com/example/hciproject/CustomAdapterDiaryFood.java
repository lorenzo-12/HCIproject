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
    ArrayList<String> username_list, date_list, or_list, food_list, exercise_list, quantity_list, sets_list, reps_list;
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

        public TextView name_txt,quantity_txt,carb_txt,prot_txt,fat_txt,kal_txt;
        public ImageView mdeletebtn,mupdatebtn,food_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.food_name_diary);
            quantity_txt = itemView.findViewById(R.id.quantity_name_diary);
            carb_txt = itemView.findViewById(R.id.food_carb_diary);
            prot_txt = itemView.findViewById(R.id.food_prot_diary);
            fat_txt = itemView.findViewById(R.id.food_fat_diary);
            kal_txt = itemView.findViewById(R.id.food_kal_diary);
            mdeletebtn = itemView.findViewById(R.id.delete_food_img_diary);
            mupdatebtn = itemView.findViewById(R.id.modify_food_img_diary);
            food_img = itemView.findViewById(R.id.food_img_diary);
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
        View view = inflater.inflate(R.layout.row_food_diary,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterDiaryFood.MyViewHolder holder, int position) {

        holder.name_txt.setText(String.valueOf(food_list.get(position)));
        Integer int_q = Integer.valueOf(quantity_list.get(position))*100;
        String str_q = String.valueOf(int_q)+"g";
        holder.quantity_txt.setText(String.valueOf(str_q));
        Bitmap b = db.loadImage(String.valueOf(food_list.get(position)));
        holder.food_img.setImageBitmap(b);

        String name_food = String.valueOf(food_list.get(position));
        int_q = Integer.valueOf(quantity_list.get(position));
        ArrayList<Integer> food_info = db.getFoodInfo(name_food);
        if (food_info.size()>0){
            holder.carb_txt.setText(String.valueOf(food_info.get(0)*int_q));
            holder.prot_txt.setText(String.valueOf(food_info.get(1)*int_q));
            holder.fat_txt.setText(String.valueOf(food_info.get(2)*int_q));
            holder.kal_txt.setText(String.valueOf(food_info.get(3)*int_q));
        }

    }

    @Override
    public int getItemCount() {
        return username_list.size();
    }
    CustomAdapterDiaryFood(Context context, ArrayList username_list_diary, ArrayList date_list_diary, ArrayList or_list_diary, ArrayList food_list_diary, ArrayList exercise_list_diary, ArrayList quantity_list_diary, ArrayList sets_list_diary, ArrayList reps_list_diary, ArrayList food_img_list_diary ){
        this.context = context;
        this.username_list = username_list_diary;
        this.date_list = date_list_diary;
        this.or_list = or_list_diary;
        this.food_list = food_list_diary;
        this.exercise_list = exercise_list_diary;
        this.quantity_list = quantity_list_diary;
        this.sets_list = sets_list_diary;
        this.reps_list = reps_list_diary;
        this.food_img_list = food_img_list_diary;
    }

}
