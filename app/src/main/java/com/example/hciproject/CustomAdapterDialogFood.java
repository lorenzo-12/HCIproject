package com.example.hciproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterDialogFood extends RecyclerView.Adapter<CustomAdapterDialogFood.MyViewHolder> {

    public OnItemClickListener mlistener;
    Context context;
    ArrayList<String> food_list;
    ArrayList<Bitmap> food_img_list;
    DBHelper db;

    public interface OnItemClickListener {
        void onQuantityClick(int position);
        void onSelectClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt;
        public EditText quantity_txt;
        public CheckBox mselectbtn;
        public ImageView food_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.food_name_dialog);
            mselectbtn = itemView.findViewById(R.id.select_food_dialog);
            food_img = itemView.findViewById(R.id.food_img_dialog);
            quantity_txt = itemView.findViewById(R.id.dialog_quantity_txt);
            db = new DBHelper(context);

            mselectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlistener.onSelectClick(position);
                        }
                    }
                }
            });

            quantity_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlistener.onQuantityClick(position);
                        }
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public CustomAdapterDialogFood.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_food_dialog,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterDialogFood.MyViewHolder holder, int position) {

        holder.name_txt.setText(String.valueOf(food_list.get(position)));
        Bitmap b = db.loadImage(String.valueOf(food_list.get(position)));
        holder.food_img.setImageBitmap(b);

    }

    @Override
    public int getItemCount() {
        return food_list.size();
    }
    CustomAdapterDialogFood(Context context, ArrayList food_list_dialog, ArrayList food_img_list_dialog ){
        this.context = context;
        this.food_list = food_list_dialog;
        this.food_img_list = food_img_list_dialog;
    }

}
