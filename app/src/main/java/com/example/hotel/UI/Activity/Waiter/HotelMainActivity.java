package com.example.hotel.UI.Activity.Waiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel.UI.Adapter.Activity.HotelAdapter;
import com.example.hotel.Core.Utils.Constants;
import com.example.hotel.Core.Model.FoodData;
import com.example.hotel.Core.Model.FoodItem;
import com.example.hotel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HotelMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<FoodData> ChineseDataList = new ArrayList<>();
    private ArrayList<FoodData> PunjabiDataList = new ArrayList<>();
    private ArrayList<FoodData> GujaratiDataList = new ArrayList<>();
    private ArrayList<FoodData> AllDataList = new ArrayList<>();
    private RecyclerView AllrecyclerView, chineserecyclerView, punjabiRecyclerview, GujaratiRecyclerview;
    private TextView txtTableNo, emptyView;
    private ImageView btnBack;
    private HotelAdapter AdapterChinese;
    private HotelAdapter AdapterPunjabi;
    private HotelAdapter AdapterGujarati;
    private HotelAdapter AdapterAll;
    private static final String TAG = "HotelMainActivity";
    private ProgressBar progressBar;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        txtTableNo = findViewById(R.id.txtTableNo);
        emptyView = findViewById(R.id.empty_view);
        btnBack = findViewById(R.id.btnBack);
        AllrecyclerView = findViewById(R.id.all_recyclerview);
        chineserecyclerView = findViewById(R.id.chinese_recyclerview);
        punjabiRecyclerview = findViewById(R.id.punjabi_recyclerview);
        GujaratiRecyclerview = findViewById(R.id.gujarati_recyclerview);
        Spinner spinnerCategory = findViewById(R.id.category_spinner);
        progressBar = findViewById(R.id.progress_bar);


        Intent intent = getIntent();
        String tableName = intent.getStringExtra("TableName");
        Log.i(TAG, "onCreate: " + tableName);

        setTitle(tableName);
        txtTableNo.setText(tableName);
        btnBack.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup Spinner
        setupSpinner(spinnerCategory);

        // Fetch and categorize food items from Firestore
        fetchFoodItemsFromFirestore();
    }

    private void setupRecyclerViews() {
        AdapterChinese = new HotelAdapter(ChineseDataList, this);
        AdapterPunjabi = new HotelAdapter(PunjabiDataList, this);
        AdapterGujarati = new HotelAdapter(GujaratiDataList, this);
        AdapterAll = new HotelAdapter(AllDataList, this);

        // Set up item click listeners
        setupItemClickListeners();

        // Set up RecyclerViews
        AllrecyclerView.setAdapter(AdapterAll); // Default to "All"
        chineserecyclerView.setAdapter(AdapterChinese);
        punjabiRecyclerview.setAdapter(AdapterPunjabi);
        GujaratiRecyclerview.setAdapter(AdapterGujarati);

        // Use LinearLayoutManager for RecyclerViews
        LinearLayoutManager ALlLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager ChineseLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager PunjabiLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager GujaratiLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        AllrecyclerView.setLayoutManager(ALlLayoutManager);
        chineserecyclerView.setLayoutManager(ChineseLayoutManager);
        punjabiRecyclerview.setLayoutManager(PunjabiLayoutManager);
        GujaratiRecyclerview.setLayoutManager(GujaratiLayoutManager);

        // Initially show only the "All" RecyclerView
        showRecyclerView("All");
    }

    private void setupItemClickListeners() {
        AdapterChinese.ItemClickListener(position -> navigateToItemDetails(ChineseDataList.get(position)));
        AdapterPunjabi.ItemClickListener(position -> navigateToItemDetails(PunjabiDataList.get(position)));
        AdapterGujarati.ItemClickListener(position -> navigateToItemDetails(GujaratiDataList.get(position)));
        AdapterAll.ItemClickListener(position -> navigateToItemDetails(AllDataList.get(position)));
    }

    private void setupSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                showRecyclerView(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle when no item is selected
            }
        });
    }

    private void showRecyclerView(String category) {
        boolean isDataAvailable = false;

        AllrecyclerView.setVisibility(View.GONE);
        chineserecyclerView.setVisibility(View.GONE);
        punjabiRecyclerview.setVisibility(View.GONE);
        GujaratiRecyclerview.setVisibility(View.GONE);

        switch (category) {
            case "Chinese":
                if (!ChineseDataList.isEmpty()) {
                    chineserecyclerView.setVisibility(View.VISIBLE);
                    isDataAvailable = true;
                }
                break;
            case "Punjabi":
                if (!PunjabiDataList.isEmpty()) {
                    punjabiRecyclerview.setVisibility(View.VISIBLE);
                    isDataAvailable = true;
                }
                break;
            case "Gujarati":
                if (!GujaratiDataList.isEmpty()) {
                    GujaratiRecyclerview.setVisibility(View.VISIBLE);
                    isDataAvailable = true;
                }
                break;
            case "All":
                if (!AllDataList.isEmpty()) {
                    AllrecyclerView.setVisibility(View.VISIBLE);
                    isDataAvailable = true;
                }
                break;
        }
        // Show empty view if no data is available
        emptyView.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
    }

    private void fetchFoodItemsFromFirestore() {
        // Clear lists before fetching new data
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setText(R.string.please_wait);
        ChineseDataList.clear();
        PunjabiDataList.clear();
        GujaratiDataList.clear();
        AllDataList.clear();

        CollectionReference foodItemsRef = db.collection("foodItems");

        foodItemsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot document : task.getResult()) {
                        FoodItem foodItem = document.toObject(FoodItem.class);
                        if (foodItem != null) {
                            categorizeAndAddFoodItem(foodItem);
                        }
                    }
                    updateAdapters();
                    showRecyclerView("All"); // Refresh view to ensure "All" is updated
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void categorizeAndAddFoodItem(FoodItem foodItem) {
        FoodData foodData = new FoodData(
                foodItem.getImageUrl(),
                foodItem.getName(),
                foodItem.getDescription(),
                String.valueOf(foodItem.getFullprice()),
                String.valueOf(foodItem.getHalfPrice())
        );

        switch (foodItem.getCategory()) {
            case "Chinese":
                ChineseDataList.add(foodData);
                break;
            case "Punjabi":
                PunjabiDataList.add(foodData);
                break;
            case "Gujarati":
                GujaratiDataList.add(foodData);
                break;
        }

        // Always add to "All" list
        if (!AllDataList.contains(foodData)) {
            AllDataList.add(foodData);
        }
    }

    private void updateAdapters() {
        AdapterChinese.notifyDataSetChanged();
        AdapterPunjabi.notifyDataSetChanged();
        AdapterGujarati.notifyDataSetChanged();
        AdapterAll.notifyDataSetChanged();
    }

    private void navigateToItemDetails(FoodData foodData) {
        Intent intent = new Intent(HotelMainActivity.this, ItemsDetails.class);
        Log.i(TAG, "navigateToItemDetails: foodData.getFoodImage() " + foodData.getFoodImage());
        intent.putExtra(Constants.FOOD_IMAGE, foodData.getFoodImage());
        intent.putExtra(Constants.FOOD_NAME, foodData.getName());
        intent.putExtra(Constants.FOOD_FULL_PRICE, foodData.getFullPrice());
        intent.putExtra(Constants.FOOD_HALF_PRICE, foodData.getHalfPrice());
        intent.putExtra(Constants.TABLE_NO, txtTableNo.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            finish(); // Close the activity
        }
    }
}
