package com.example.hciproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterDialogItem extends RecyclerView.Adapter<CustomAdapterDialogItem.MyViewHolder> {

    public OnItemClickListener mlistener;
    Context context;
    ArrayList<String> item_list;
    ArrayList<Bitmap> food_img_list;
    DBHelper db;

    public interface OnItemClickListener {
        void onQuantityClick(int position, MyViewHolder v);
        void onQuantity2Click(int position, MyViewHolder v);
        //void onSelectClick(int position, MyViewHolder v);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt;
        public EditText quantity_txt,quantity2_txt;
        public CheckBox mselectbtn;
        public ImageView food_img;
        public MyViewHolder vh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.food_name_dialog);
            mselectbtn = itemView.findViewById(R.id.select_food_dialog);
            food_img = itemView.findViewById(R.id.food_img_dialog);
            quantity_txt = itemView.findViewById(R.id.dialog_quantity_txt);
            quantity2_txt = itemView.findViewById(R.id.dialog_quantity2_txt);
            vh = this;
            db = new DBHelper(context);
            vh.mselectbtn.setClickable(false);
            vh.mselectbtn.setFocusable(false);

            quantity_txt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

            quantity_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mlistener.onQuantityClick(position,vh);
                    }
                }
            });

            quantity_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //nulla
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //nulla
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mlistener.onQuantityClick(position,vh);
                    }
                }
            });

            quantity_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            //mlistener.onQuantityClick(position,vh);
                        }
                    }
                }
            });

            quantity2_txt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

            quantity2_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mlistener.onQuantity2Click(position,vh);
                    }
                }
            });

            quantity2_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //nulla
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //nulla
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mlistener.onQuantity2Click(position,vh);
                    }
                }
            });

            quantity2_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            //mlistener.onQuantity2Click(position,vh);
                        }
                    }
                }
            });

            /*
            mselectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mlistener.onSelectClick(position,vh);
                        }
                    }
                }
            });

             */

        }
    }

    @NonNull
    @Override
    public CustomAdapterDialogItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_food_dialog,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterDialogItem.MyViewHolder holder, int position) {

        holder.name_txt.setText(String.valueOf(item_list.get(position)));
        Bitmap b = db.loadImage(String.valueOf(item_list.get(position)));
        holder.food_img.setImageBitmap(b);

        Boolean cor = db.findFood(item_list.get(position).toString().toLowerCase());
        if (cor){
            holder.quantity2_txt.setVisibility(View.INVISIBLE);
        }
        else{
            holder.quantity2_txt.setVisibility(View.VISIBLE);
            holder.quantity_txt.setHint("set");
        }

    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }
    CustomAdapterDialogItem(Context context, ArrayList item_list_dialog, ArrayList food_img_list_dialog ){
        this.context = context;
        this.item_list = item_list_dialog;
        this.food_img_list = food_img_list_dialog;
    }

}
