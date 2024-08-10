package com.example.hotel.UI.Fragment.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotel.R;
import com.example.hotel.UI.Activity.Admin.AdminLoginActivity;
import com.example.hotel.Core.Utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class AdminProfileFragment extends Fragment {

    private ImageView adminProfilePhoto;
    private TextView adminFullName;
    private TextView adminEmail;
    private Button btnSignOut;
    private SessionManager sessionManager;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        adminProfilePhoto = view.findViewById(R.id.admin_profile_photo);
        adminFullName = view.findViewById(R.id.admin_full_name);
        adminEmail = view.findViewById(R.id.admin_email);
        btnSignOut = view.findViewById(R.id.btn_sign_out);
        sessionManager = new SessionManager(getContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Admins");

        // Load admin profile data
        loadAdminProfile();

        // Set sign out button listener
        btnSignOut.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Intent intent = new Intent(getActivity(), AdminLoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    private void loadAdminProfile() {
        String uid = mAuth.getCurrentUser().getUid(); // Get current user UID
        if (uid != null) {
            mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String profilePhotoUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        // Set the full name
                        String fullName = firstName + " " + lastName;
                        adminFullName.setText(fullName);
                        adminEmail.setText(email);

                        // Load profile image
                        Glide.with(AdminProfileFragment.this)
                                .load(profilePhotoUrl)
                                .placeholder(R.drawable.ic_add)
                                .into(adminProfilePhoto);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
}
