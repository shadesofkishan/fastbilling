package com.example.fastbilling.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastbilling.R;
import com.example.fastbilling.model.ItemsModel;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<ItemsModel> dataHolder;

    public RecyclerAdapter(ArrayList<ItemsModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_customdesign,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemsModel imageModel = dataHolder.get(position);

        Log.e("RecyclerView","Image Url here: "+imageModel.getImageUrl());
        Log.e("RecyclerView","Image Name here: "+imageModel.getImageName());

        holder.dTitle.setText(dataHolder.get(position).getTitle());
        holder.dPrice.setText(dataHolder.get(position).getPrice());
        holder.dIMEI.setText(dataHolder.get(position).getImei());
        Glide.with(holder.itemView.getContext())
                .load(imageModel.getImageUrl())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView dTitle,dPrice,dIMEI;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dTitle = itemView.findViewById(R.id.rTitle);
            dPrice = itemView.findViewById(R.id.rPrice);
            dIMEI = itemView.findViewById(R.id.rImei);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

}
