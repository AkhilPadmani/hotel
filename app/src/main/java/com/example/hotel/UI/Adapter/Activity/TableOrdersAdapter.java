package com.example.hotel.UI.Adapter.Activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel.Core.Model.Order;
import com.example.hotel.R;
import com.example.hotel.UI.Activity.Waiter.TableOrdersDetailsActivity;

import java.util.List;
import java.util.Map;

public class TableOrdersAdapter extends RecyclerView.Adapter<TableOrdersAdapter.TableViewHolder> {

    private Map<String, List<Order>> tableOrdersMap;
    private Activity context;
    private static final String TAG="TableOrdersAdapter";

    public TableOrdersAdapter(Map<String, List<Order>> tableOrdersMap, Activity context) {
        this.tableOrdersMap = tableOrdersMap;
        this.context=context;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        String tableNo = (String) tableOrdersMap.keySet().toArray()[position];
        List<Order> orders = tableOrdersMap.get(tableNo);

        holder.tableNo.setText("Table No: " + tableNo);
        holder.itemView.setOnClickListener(v -> {
            Log.i(TAG, "onBindViewHolder: ");
            // Handle click to navigate to detail fragment
            Intent args = new Intent(context, TableOrdersDetailsActivity.class);
            args.putExtra("tableNo", tableNo);
            Log.i(TAG, "onBindViewHolder: ***");
            context.startActivity(args);
        });
    }

    @Override
    public int getItemCount() {
        return tableOrdersMap.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {

        TextView tableNo;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNo = itemView.findViewById(R.id.txtTableNo);
        }
    }
}
