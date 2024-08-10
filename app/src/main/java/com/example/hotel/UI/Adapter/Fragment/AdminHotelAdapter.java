package com.example.hotel.UI.Adapter.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hotel.Core.Model.FoodData;
import com.example.hotel.R;
import com.example.hotel.UI.Interface.OnItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminHotelAdapter extends RecyclerView.Adapter<AdminHotelAdapter.ViewHolder> {
    private ArrayList<FoodData> dataList;
    private Context context;
    private static final String TAG = "RecyclerViewAdapter";
    private OnItemClickListener onItemClickListener;

    public AdminHotelAdapter(ArrayList<FoodData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public void ItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_food_item, parent, false);
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

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.ItemClick(position);
                }
            }
        });*/

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the custom layout
                LayoutInflater inflater = LayoutInflater.from(context);
                View customView = inflater.inflate(R.layout.dialog_custom, null);

                // Create the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(customView);

                // Set the background of the dialog to be transparent
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                // Initialize the buttons and text in the custom layout
                TextView tvDialogMessage = customView.findViewById(R.id.tvDialogMessage);
                Button btnConfirm = customView.findViewById(R.id.btnConfirm);
                Button btnCancel = customView.findViewById(R.id.btnCancel);
                dialog.setCancelable(false);
                // Set button click listeners
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User confirmed, proceed with deletion
                        deleteItem(data, position);
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User cancelled, dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Show the dialog
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView Food;
        MaterialTextView Name, Desc, Price;
        ProgressBar progressBar;
        MaterialCardView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Food = itemView.findViewById(R.id.img_food);
            progressBar = itemView.findViewById(R.id.progress_bar);
            Name = itemView.findViewById(R.id.txtfoodname);
            Desc = itemView.findViewById(R.id.txtFoodDescription);
            Price = itemView.findViewById(R.id.txtFoodPrice);
            btnDelete = itemView.findViewById(R.id.LayItemAdd);
        }
    }

    private void deleteItem(FoodData foodData, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodItemsRef = db.collection("foodItems");

        foodItemsRef.whereEqualTo("name", foodData.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot document : task.getResult()) {
                                foodItemsRef.document(document.getId()).delete();
                            }
                            // Remove the item from the local list and notify the adapter
                            dataList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, dataList.size());
                        } else {
                            Log.w(TAG, "Error deleting document", task.getException());
                        }
                    }
                });
    }
}
