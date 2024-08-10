package com.example.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hotel.UI.Activity.Admin.AdminLoginActivity;
import com.example.hotel.UI.Activity.Admin.AdminRegisterActivity;
import com.example.hotel.UI.Fragment.Waiter.HomeFragment;
import com.example.hotel.UI.Fragment.Waiter.TableOrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    HomeFragment homeFragment = new HomeFragment();
    TableOrdersFragment TableOrdersFragment = new TableOrdersFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                /*                switch (item.getItemId()) {
                 *//* case R.id.home:
                        Intent i1 = new Intent(navigationbar.this, sendOtp.class);
                        startActivity(i1);
                        break;*//*
//                    case R.id.rate:
//                        // Toast.makeText(getApplicationContext(), "HELLO USER", Toast.LENGTH_SHORT).show();
//                        Intent i2 = new Intent(navigationbar.this, retrieve.class);
//                        startActivity(i2);
//                        break;
//                    case R.id.Community:
//                        Intent i3 = new Intent(navigationbar.this, Add_group.class);
//                        startActivity(i3);
//                        break;
//                    case R.id.person_:
//                        Intent i4 = new Intent(navigationbar.this, Profile.class);
//                        startActivity(i4);
//                        break;

                }*/
                int id = item.getItemId();
                if (id == R.id.adminlogin) {
                    Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                    startActivity(intent);
                } else if (id == R.id.adminregister) {
                    Intent intent = new Intent(MainActivity.this, AdminRegisterActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();

//        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.notification);
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(8);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.Home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                    return true;
                } else if (id == R.id.Setting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, TableOrdersFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void update() {
        navigationView = findViewById(R.id.nav);
        View view = navigationView.getHeaderView(0);
        TextView email = view.findViewById(R.id.header_email);
        TextView name = view.findViewById(R.id.header_name);
    }

    private void fragmentR(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}