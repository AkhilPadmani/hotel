package com.example.hotel.UI.Activity.Waiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel.UI.Adapter.Activity.OrdersAdapter;
import com.example.hotel.Core.Model.Order;
import com.example.hotel.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TableOrdersDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private FirebaseFirestore db;
    private String tableNo;
    private ImageView btnBack;
    private TextView txtItemName;
    private static final String TAG = "TableOrdersDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tableorders_detail);
        this.recyclerView = findViewById(R.id.recyclerViewOrderDetails);
        this.btnBack = findViewById(R.id.btnBack);
        this.txtItemName = findViewById(R.id.txtItemName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        tableNo = intent.getStringExtra("tableNo");
        if (txtItemName != null) {
            txtItemName.setText(tableNo);
        }
        Log.i(TAG, "onCreate: " + tableNo);
        fetchOrderDetails();

        if (btnBack != null) {
            btnBack.setOnClickListener(this);
        }

    }

    private void fetchOrderDetails() {
        db.collection("cart")
                .whereEqualTo("tableNo", tableNo)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        List<Order> orders = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Order order = doc.toObject(Order.class);
                            orders.add(order);
                        }
                        ordersAdapter = new OrdersAdapter(orders);
                        recyclerView.setAdapter(ordersAdapter);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            TableOrdersDetailsActivity.this.finish();
        }
    }
}
