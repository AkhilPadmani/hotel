package com.example.hotel.UI.Activity.Waiter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hotel.Core.Utils.Constants;
import com.example.hotel.Core.Model.AddedData;
import com.example.hotel.R;
import com.example.hotel.UI.Fragment.Waiter.TableOrdersFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ItemsDetails extends AppCompatActivity implements View.OnClickListener {
    private KonfettiView konfettiView;
    private ImageView imageView, btnBack;
    private TextView txtFoodName, txtFoodFullPrice, txtHalfFullPrice, txtItemName, txtQuantity, txtTotalAmount;
    private ImageView btnIncrease, btnDecrease;
    private RadioGroup radioGroupPrice;
    private RadioButton radioFullPrice, radioHalfPrice;
    private RelativeLayout ParentLayout;
    private double itemPrice, halfPrice;
    private static final String TAG = "ItemsDetails";
    private String imageResId;
    private String FoodName, FoodFullPrice,FoodHalfPrice, ItemName, TableNo;
    private int quantity = 1;
    private boolean isFullPrice = true;
    private Button BtnSubmit;

    // Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        db = FirebaseFirestore.getInstance();

        this.imageView = findViewById(R.id.imageView);
        this.btnBack = findViewById(R.id.btnBack);
        this.txtFoodName = findViewById(R.id.txtFoodName);
        this.txtFoodFullPrice = findViewById(R.id.txtFoodFullPrice);
        this.txtHalfFullPrice = findViewById(R.id.txtFoodHalfPrice);
        this.txtItemName = findViewById(R.id.txtItemName);
        this.txtQuantity = findViewById(R.id.txtQuantity);
        this.btnIncrease = findViewById(R.id.btnIncrease);
        this.btnDecrease = findViewById(R.id.btnDecrease);
        this.txtTotalAmount = findViewById(R.id.txtTotalAmount);
        this.radioGroupPrice = findViewById(R.id.radioGroupPrice);
        this.radioFullPrice = findViewById(R.id.radioFullPrice);
        this.radioHalfPrice = findViewById(R.id.radioHalfPrice);
        this.BtnSubmit = findViewById(R.id.BtnSubmit);
        this.konfettiView = findViewById(R.id.KonfettiView);
        this.ParentLayout = findViewById(R.id.ParentLayout);


        Intent intent = getIntent();
        imageResId = intent.getStringExtra(Constants.FOOD_IMAGE);
        FoodName = intent.getStringExtra(Constants.FOOD_NAME);
        FoodFullPrice = intent.getStringExtra(Constants.FOOD_FULL_PRICE);
        FoodHalfPrice = intent.getStringExtra(Constants.FOOD_HALF_PRICE);
        TableNo = intent.getStringExtra(Constants.TABLE_NO);
        Log.i(TAG, "FoodFullPrice " + FoodFullPrice);
        Log.i(TAG, "FoodHalfPrice " + FoodHalfPrice);
        ItemName = intent.getStringExtra(Constants.FOOD_NAME);


        Glide.with(ItemsDetails.this).load(imageResId).into(imageView);


        txtFoodName.setText(FoodName);
        txtFoodFullPrice.setText(Constants.FULL_FOOD + FoodFullPrice);
        FoodFullPrice = FoodFullPrice.replaceAll("[^\\d.]", "");
        itemPrice = Double.parseDouble(FoodFullPrice);
        halfPrice = Double.parseDouble(FoodHalfPrice);

        txtHalfFullPrice.setText(Constants.HALF_FOOD +FoodHalfPrice);
        txtItemName.setText(ItemName);
        if (btnBack != null) {
            btnBack.setOnClickListener(this);
        }

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodItemToCart();
                // Show Snackbar message
                Snackbar.make(v, "Items added into cart", Snackbar.LENGTH_SHORT).show();

                SuccessFullyAdded();
                // Replace the current view with the TableOrdersFragment
                // loadTableOrdersFragment();
            }
        });


        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        radioGroupPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioFullPrice) {
                    isFullPrice = true;
                } else if (checkedId == R.id.radioHalfPrice) {
                    isFullPrice = false;
                }
                updateTotalAmount();
            }
        });

        txtQuantity.setText(String.valueOf(quantity));
        updateTotalAmount();
    }

    private void loadTableOrdersFragment() {
        ParentLayout.setVisibility(View.GONE);
        TableOrdersFragment tableOrdersFragment = new TableOrdersFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, tableOrdersFragment)
                .addToBackStack(null) // This will add the transaction to the back stack
                .commit();

        // Make the fragment container visible
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            ItemsDetails.this.finish();
        }
    }

    private void increaseQuantity() {
        quantity++;
        txtQuantity.setText(String.valueOf(quantity));
        updateTotalAmount();
    }

    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            txtQuantity.setText(String.valueOf(quantity));
            updateTotalAmount();
        }
    }

    private void updateTotalAmount() {
        double totalAmount = quantity * (isFullPrice ? itemPrice : halfPrice);
        txtTotalAmount.setText(String.format("₹ %.2f", totalAmount));
    }

    private void addFoodItemToCart() {
        String foodName = txtFoodName.getText().toString();
        String itemName = txtItemName.getText().toString();
        int qty = Integer.parseInt(txtQuantity.getText().toString());
        double piecePrice = isFullPrice ? itemPrice : halfPrice;
        double totalAmount = qty * piecePrice;
        String tableNo = TableNo;  // Retrieve the table number

        AddedData foodItem = new AddedData(foodName, itemName, qty, piecePrice, totalAmount, isFullPrice, tableNo);  // Pass the table number

        db.collection("cart")
                .add(foodItem)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    public void SuccessFullyAdded() {
        konfettiView.build()
                .addColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(10, 5))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(400, 2000L);

    }
}
