package com.example.hotel.UI.Fragment.Admin;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotel.Core.Model.FoodItem;
import com.example.hotel.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class AddNewItemFragment extends Fragment {

    private static final String TAG = "AddFoodFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final String CHANNEL_ID = "food_notification_channel";

    private EditText editTextName, editTextDescription, editTextPrice, editTextHalfPrice;
    private Spinner spinnerCategory;
    private TextView buttonAddFood;
    private ImageView buttonSelectImage, btnBack;
    private CircleImageView imageViewSelected;
    private Uri imageUri;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private KonfettiView konfettiView;
    private ProgressBar progressBar;

    public AddNewItemFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);

        this.editTextName = view.findViewById(R.id.editTextName);
        this.konfettiView = view.findViewById(R.id.KonfettiView);
        this.editTextDescription = view.findViewById(R.id.editTextDescription);
        this.editTextPrice = view.findViewById(R.id.editTextPrice);
        this.editTextHalfPrice = view.findViewById(R.id.editTextHalfPrice);
        this.spinnerCategory = view.findViewById(R.id.spinnerCategory);
        this.buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        this.btnBack = view.findViewById(R.id.btnBack);
        this.buttonAddFood = view.findViewById(R.id.buttonAddFood);
        this.imageViewSelected = view.findViewById(R.id.imageViewSelected);
        this.progressBar = view.findViewById(R.id.progress_bar);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("foodImages");

        // Set up Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonSelectImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                openFileChooser();
            }
        });

        buttonAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewFood();
            }
        });
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        createNotificationChannel();
        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageViewSelected);
        }
    }

    private void addNewFood() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String halfPriceString = editTextHalfPrice.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceString)
                || TextUtils.isEmpty(halfPriceString)
                || TextUtils.isEmpty(category) || imageUri == null) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buttonAddFood.setVisibility(View.GONE);

        double price = Double.parseDouble(priceString);
        double halfPrice = Double.parseDouble(halfPriceString);

        final String randomKey = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child(randomKey);

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    FoodItem foodItem = new FoodItem(name, description, price, halfPrice, imageUrl, category);

                    db.collection("foodItems")
                            .add(foodItem)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonAddFood.setVisibility(View.VISIBLE);
                                    showNotification(name);
                                    SuccessFullyAdded();
                                    editTextName.setText("");
                                    editTextDescription.setText("");
                                    editTextPrice.setText("");
                                    editTextHalfPrice.setText("");
                                    imageViewSelected.setImageDrawable(null);
                                    spinnerCategory.setSelected(false);
                                } else {
                                    Toast.makeText(getContext(), "Error adding food item", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void SuccessFullyAdded() {
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FoodNotificationChannel";
            String description = "Channel for food item notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String foodName) {
        // Set the notification sound URI
        Uri soundUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.new_video);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Food Item Added")
                .setContentText(foodName+" has been added successfully!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(soundUri); // Add custom sound

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClearAllMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ClearAllMemory();
    }

    private void ClearAllMemory() {
        if (btnBack != null) {
            btnBack = null;
        }
        if (buttonAddFood != null) {
            buttonAddFood = null;
        }
        if (buttonSelectImage != null) {
            buttonSelectImage = null;
        }
        if (editTextName != null) {
            editTextName = null;
        }
        if (konfettiView != null) {
            konfettiView = null;
        }
        if (editTextDescription != null) {
            editTextDescription = null;
        }
        if (editTextPrice != null) {
            editTextPrice = null;
        }
        if (editTextHalfPrice != null) {
            editTextHalfPrice = null;
        }
        if (spinnerCategory != null) {
            spinnerCategory = null;
        }
        if (imageViewSelected != null) {
            imageViewSelected = null;
        }
        if (imageUri != null) {
            imageUri = null;
        }
        if (progressBar != null) {
            progressBar = null;
        }
        if (storageReference != null) {
            storageReference = null;
        }
    }
}
