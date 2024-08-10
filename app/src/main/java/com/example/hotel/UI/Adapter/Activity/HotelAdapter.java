package com.example.hotel.UI.Adapter.Activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hotel.Core.Model.FoodData;
import com.example.hotel.R;
import com.example.hotel.UI.Interface.OnItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private ArrayList<FoodData> dataList;
    private Context context;
    private static final String TAG = "RecyclerViewAdapter";
    private OnItemClickListener onItemClickListener;

    public HotelAdapter(ArrayList<FoodData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public void ItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodData data = dataList.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(data.getFoodImage())
                .placeholder(R.drawable.ic_error) // Optional placeholder
                .error(R.drawable.ic_add) // Optional error image
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Hide ProgressBar when image is loaded
                        holder.progressBar.setVisibility(View.GONE);
                        holder.Food.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Hide ProgressBar if the load is cleared
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });
        holder.Name.setText(data.getName());
        holder.Desc.setText(data.getDescription());
        holder.Price.setText(data.getFullPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.ItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView Food, Food2;
        MaterialTextView Name, Desc, Price, Name2, Desc2, Price2;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Food = itemView.findViewById(R.id.img_food);
            progressBar = itemView.findViewById(R.id.progress_bar);
            Name = itemView.findViewById(R.id.txtfoodname);
            Desc = itemView.findViewById(R.id.txtFoodDescription);
            Price = itemView.findViewById(R.id.txtFoodPrice);
        }
    }
}
