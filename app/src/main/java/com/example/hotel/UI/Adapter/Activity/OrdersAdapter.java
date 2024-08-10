package com.example.hotel.UI.Adapter.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel.Core.Model.Order;
import com.example.hotel.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tableNo.setText("Table No: " + order.getTableNo());
        holder.foodName.setText("Food: " + order.getFoodName());
        holder.quantity.setText("Quantity: " + order.getQuantity());
        holder.totalAmount.setText("Total: ₹ " + order.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tableNo, foodName, quantity, totalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNo = itemView.findViewById(R.id.txtTableNo);
            foodName = itemView.findViewById(R.id.txtFoodName);
            quantity = itemView.findViewById(R.id.txtQuantity);
            totalAmount = itemView.findViewById(R.id.txtTotalAmount);
        }
    }
}
