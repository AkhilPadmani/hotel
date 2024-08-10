package com.example.hotel.UI.Activity.Admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hotel.R;
import com.example.hotel.UI.Fragment.Admin.AddNewItemFragment;
import com.example.hotel.UI.Fragment.Admin.AddedItemFragment;
import com.example.hotel.UI.Fragment.Admin.AdminProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();
                if (id == R.id.nav_added_item) {
                    fragment = new AddedItemFragment();
                } else if (id == R.id.nav_add_new_item) {
                    fragment = new AddNewItemFragment();
                } else if (id == R.id.nav_admin_profile) {
                    fragment = new AdminProfileFragment();
                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });

        // Load the default fragment
        bottomNavigationView.setSelectedItemId(R.id.nav_added_item);
    }
}
