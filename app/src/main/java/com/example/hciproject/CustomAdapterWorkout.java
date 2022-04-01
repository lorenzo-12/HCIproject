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

public class CustomAdapterWorkout extends RecyclerView.Adapter<CustomAdapterWorkout.MyViewHolder>{

    public static final String CNAME_WORKOUT = "workout_name";
    public CustomAdapterWorkout.OnItemClickListener mlistener;
    Context context;
    ArrayList<String> workout_name_list, workout_category_list, workout_reps_list, workout_series_list;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }

    public void setOnItemClickListener(CustomAdapterWorkout.OnItemClickListener listener){
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt,category_txt,reps_txt,series_txt,number_txt;
        public ImageView mdeletebtn,mupdatebtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.workout_name);
            category_txt = itemView.findViewById(R.id.workout_category);
            reps_txt = itemView.findViewById(R.id.workout_reps);
            series_txt = itemView.findViewById(R.id.workout_series);
            mdeletebtn = itemView.findViewById(R.id.delete_workout_img);
            mupdatebtn = itemView.findViewById(R.id.modify_workout_img);
            number_txt = itemView.findViewById(R.id.number_txt_workout);

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
        View view = inflater.inflate(R.layout.my_row_workout,parent,false);
        return new CustomAdapterWorkout.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_txt.setText(String.valueOf(workout_name_list.get(position)));
        holder.category_txt.setText(String.valueOf(workout_category_list.get(position)));
        holder.reps_txt.setText(String.valueOf(workout_reps_list.get(position)));
        holder.series_txt.setText(String.valueOf(workout_series_list.get(position)));
        holder.number_txt.setText(String.valueOf(position));
    }


    @Override
    public int getItemCount() {
        return workout_name_list.size();
    }

    CustomAdapterWorkout(Context context, ArrayList list_name, ArrayList list_category, ArrayList list_reps, ArrayList list_series){
        this.context = context;
        this.workout_name_list = list_name;
        this.workout_category_list = list_category;
        this.workout_reps_list = list_reps;
        this.workout_series_list = list_series;
    }

}
