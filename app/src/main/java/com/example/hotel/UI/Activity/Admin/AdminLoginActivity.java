package com.example.hotel.UI.Activity.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.R;
import com.example.hotel.Core.Utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    boolean PasswordVisible;
    private FirebaseAuth mAuth;
    private KonfettiView konfettiView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Redirect to AddActivity
            startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
            finish();
            return;
        }

        this.emailEditText = findViewById(R.id.UserName);
        this.passwordEditText = findViewById(R.id.Password_);
        this.loginButton = findViewById(R.id.Loginbtn);
        this.konfettiView = findViewById(R.id.KonfettiView);

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = passwordEditText.getSelectionEnd();
                        if (PasswordVisible) {
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.key_, 0, R.drawable.visibility_off, 0);
                            //hide password
                            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            PasswordVisible=false;
                        } else {
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.key_, 0, R.drawable.visibility, 0);
                            //hide password
                            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            PasswordVisible=true;
                        }
                        passwordEditText.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    sessionManager.setLoggedIn(true); // Set user as logged in
                    SuccessFullyAdded();
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        new Handler().postDelayed(() -> {
            startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
            finish();
        }, 2000);
    }
}
