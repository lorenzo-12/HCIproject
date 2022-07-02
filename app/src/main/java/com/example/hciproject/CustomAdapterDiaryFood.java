package com.example.hciproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        TextView set_reps,c,p,f,k;
        ConstraintLayout cl;

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
            set_reps = itemView.findViewById(R.id.set_reps_txt);
            c = itemView.findViewById(R.id.testo_fisso_carb_diary);
            p = itemView.findViewById(R.id.testo_fisso_prot_diary);
            f = itemView.findViewById(R.id.testo_fisso_fat_diary);
            k = itemView.findViewById(R.id.testo_fisso_kal_diary);
            db = new DBHelper(context);
            cl = itemView.findViewById(R.id.background_view_diary);

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

        String n = String.valueOf(food_list.get(position));
        String n2 = String.valueOf(exercise_list.get(position));
        Boolean f = db.findFood(n.toLowerCase());
        //Log.d("debug","item: "+n+n2+" type: "+f);
        if (f) {
            holder.name_txt.setText(String.valueOf(food_list.get(position)));
            Integer int_q = Integer.valueOf(quantity_list.get(position));
            String str_q = String.valueOf(int_q) + "g";
            holder.quantity_txt.setText(String.valueOf(str_q));
            Bitmap b = db.loadImage(String.valueOf(food_list.get(position)));
            holder.food_img.setImageBitmap(b);

            String name_food = String.valueOf(food_list.get(position));
            int_q = Integer.valueOf(quantity_list.get(position));
            ArrayList<Integer> food_info = db.getFoodInfo(name_food);
            if (food_info.size() > 0) {
                holder.carb_txt.setText(String.valueOf(food_info.get(0) * int_q /100));
                holder.prot_txt.setText(String.valueOf(food_info.get(1) * int_q /100));
                holder.fat_txt.setText(String.valueOf(food_info.get(2) * int_q /100));
                holder.kal_txt.setText(String.valueOf(food_info.get(3) * int_q /100));
            }
            holder.cl.setBackgroundColor(Color.parseColor("#0F9D58"));
            holder.set_reps.setVisibility(View.INVISIBLE);
            holder.c.setVisibility(View.VISIBLE);
            holder.p.setVisibility(View.VISIBLE);
            holder.f.setVisibility(View.VISIBLE);
            holder.k.setVisibility(View.VISIBLE);
            holder.carb_txt.setVisibility(View.VISIBLE);
            holder.prot_txt.setVisibility(View.VISIBLE);
            holder.fat_txt.setVisibility(View.VISIBLE);
            holder.kal_txt.setVisibility(View.VISIBLE);
            holder.quantity_txt.setVisibility(View.VISIBLE);

            holder.set_reps.setVisibility(View.INVISIBLE);
        }
        if(!f) {
            //Log.d("debug", "sto eseguendo questo perchè: "+n+n2+" "+f);
            holder.name_txt.setText(String.valueOf(exercise_list.get(position)));
            holder.set_reps.setText("set: "+String.valueOf(sets_list.get(position))+" rep: "+String.valueOf(reps_list.get(position)));
            Bitmap b = db.loadImage(String.valueOf(exercise_list.get(position)));
            holder.food_img.setImageBitmap(b);
            holder.cl.setBackgroundColor(Color.CYAN);
            holder.set_reps.setVisibility(View.VISIBLE);
            holder.c.setVisibility(View.INVISIBLE);
            holder.p.setVisibility(View.INVISIBLE);
            holder.f.setVisibility(View.INVISIBLE);
            holder.k.setVisibility(View.INVISIBLE);
            holder.carb_txt.setVisibility(View.INVISIBLE);
            holder.prot_txt.setVisibility(View.INVISIBLE);
            holder.fat_txt.setVisibility(View.INVISIBLE);
            holder.kal_txt.setVisibility(View.INVISIBLE);
            holder.quantity_txt.setVisibility(View.INVISIBLE);
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
