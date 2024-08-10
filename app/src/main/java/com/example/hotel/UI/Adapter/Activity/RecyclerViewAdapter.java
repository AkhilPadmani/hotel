package com.example.hotel.UI.Adapter.Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel.Core.Model.Data;
import com.example.hotel.R;
import com.example.hotel.UI.Interface.OnItemClickListener;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Data> dataList;
    private Context context;
    OnItemClickListener onItemClick;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(ArrayList<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public void setOnItemClick(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.DpImage.setImageResource(data.getDp_image());
        holder.Name.setText(data.getName());

       /* holder.LayParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if context is not null before creating Intent
                Log.i(TAG, " context "+context);
                if (context != null) {
                    *//*BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
                    bottomSheetDialog.setContentView(R.layout.food_item);
                    bottomSheetDialog.show();*//*
                    Intent intent = new Intent(context, HotelMainActivity.class);
                    intent.putExtra("data", data.getName()); // Pass data to HotelMainActivity if needed
                    context.startActivity(intent);
                }
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ******* ");
                if (onItemClick != null) {
                    Log.i(TAG, "onClick: ***2222 ");
                    int position = holder.getAdapterPosition();
                    onItemClick.ItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView DpImage;
        TextView Name;
        RelativeLayout LayParent; // Assuming R.id.parent is your LinearLayout

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DpImage = itemView.findViewById(R.id.Table_icon);
            Name = itemView.findViewById(R.id.txttablename);
            LayParent = itemView.findViewById(R.id.LayParent);
        }
    }
}
