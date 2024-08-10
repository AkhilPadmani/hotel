package com.example.hotel.UI.Fragment.Waiter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel.UI.Adapter.Activity.TableOrdersAdapter;
import com.example.hotel.Core.Model.Order;
import com.example.hotel.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private TableOrdersAdapter tableOrdersAdapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tableorders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTableOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        fetchTableOrders();

        return view;
    }

    private void fetchTableOrders() {
        db.collection("cart")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        Map<String, List<Order>> tableOrdersMap = new HashMap<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Order order = doc.toObject(Order.class);
                            String tableNo = order.getTableNo();
                            if (!tableOrdersMap.containsKey(tableNo)) {
                                tableOrdersMap.put(tableNo, new ArrayList<>());
                            }
                            tableOrdersMap.get(tableNo).add(order);
                        }

                        tableOrdersAdapter = new TableOrdersAdapter(tableOrdersMap,getActivity());
                        recyclerView.setAdapter(tableOrdersAdapter);
                    }
                });
    }
}
