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

public class CustomAdapterExercise extends RecyclerView.Adapter<CustomAdapterExercise.MyViewHolder>{

    public OnItemClickListener mlistener;
    Context context;
    ArrayList<String> exercise_name_list, exercise_category_list, exercise_reps_list, exercise_series_list;
    ArrayList<Bitmap> exercise_img_list;
    DBHelper db;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }

    public void setOnItemClickListener(CustomAdapterExercise.OnItemClickListener listener){
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt,category_txt,reps_txt,series_txt,number_txt;
        public ImageView mdeletebtn,mupdatebtn,exercise_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.exercise_name);
            category_txt = itemView.findViewById(R.id.exercise_category);
            reps_txt = itemView.findViewById(R.id.exercise_reps);
            series_txt = itemView.findViewById(R.id.exercise_series);
            mdeletebtn = itemView.findViewById(R.id.delete_workout_img);
            mupdatebtn = itemView.findViewById(R.id.modify_workout_img);
            exercise_img = itemView.findViewById(R.id.exercise_img);
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_exercise,parent,false);
        return new CustomAdapterExercise.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_txt.setText(String.valueOf(exercise_name_list.get(position)));
        holder.category_txt.setText(String.valueOf(exercise_category_list.get(position)));
        holder.reps_txt.setText(String.valueOf(exercise_reps_list.get(position)));
        holder.series_txt.setText(String.valueOf(exercise_series_list.get(position)));
        Bitmap b = db.loadImage(String.valueOf(exercise_name_list.get(position)));
        holder.exercise_img.setImageBitmap(b);
    }


    @Override
    public int getItemCount() {
        return exercise_name_list.size();
    }

    CustomAdapterExercise(Context context, ArrayList list_name, ArrayList list_category, ArrayList list_reps, ArrayList list_series, ArrayList list_img){
        this.context = context;
        this.exercise_name_list = list_name;
        this.exercise_category_list = list_category;
        this.exercise_reps_list = list_reps;
        this.exercise_series_list = list_series;
        this.exercise_img_list = list_img;
    }

}
