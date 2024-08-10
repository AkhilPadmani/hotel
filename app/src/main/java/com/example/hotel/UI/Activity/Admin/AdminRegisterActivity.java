package com.example.hotel.UI.Activity.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hotel.R;
import com.example.hotel.Core.Utils.SessionManager;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminRegisterActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtEmail, edtPassword, edtMobile;
    private AppCompatButton btnRegister;
    private ImageView profileImageView;
    private Button btnFilePicker;
    private Uri profileImageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageReference;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AdminRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Admins");
        mStorageReference = FirebaseStorage.getInstance().getReference("profile_images");
        sessionManager = new SessionManager(this);

        edtFirstName = findViewById(R.id.FName);
        edtLastName = findViewById(R.id.LName);
        edtEmail = findViewById(R.id.Email);
        edtPassword = findViewById(R.id.Password);
        edtMobile = findViewById(R.id.Mobile);
        profileImageView = findViewById(R.id.circle_image);
        btnFilePicker = findViewById(R.id.btnFilePicker);
        btnRegister = findViewById(R.id.Register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");

        btnFilePicker.setOnClickListener(view -> openFileChooser());
        btnRegister.setOnClickListener(view -> registerUser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            profileImageView.setImageURI(profileImageUri);
        }
    }

    private boolean validateInputs(String firstName, String lastName, String email, String password, String mobile) {
        if (TextUtils.isEmpty(firstName)) {
            edtFirstName.setError("First name is required");
            edtFirstName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(lastName)) {
            edtLastName.setError("Last name is required");
            edtLastName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Valid email is required");
            edtEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            edtPassword.setError("Password should be at least 6 characters");
            edtPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mobile)) {
            edtMobile.setError("Mobile number is required");
            edtMobile.requestFocus();
            return false;
        }
        if (profileImageUri == null) {
            Toast.makeText(this, "Profile image is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getFileExtension(Uri uri) {
        return getContentResolver().getType(uri).split("/")[1];
    }

    private void registerUser() {
        final String firstName = edtFirstName.getText().toString().trim();
        final String lastName = edtLastName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        final String mobile = edtMobile.getText().toString().trim();

        if (validateInputs(firstName, lastName, email, password, mobile)) {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            uploadProfileImage(firstName, lastName, email, mobile);
                        } else {
                            progressDialog.dismiss();
                            Log.e(TAG, "Registration failed: ", task.getException());
                            Toast.makeText(AdminRegisterActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void uploadProfileImage(final String firstName, final String lastName, final String email, final String mobile) {
        final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(profileImageUri));

        fileReference.putFile(profileImageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnCompleteListener(uriTask -> {
                            if (uriTask.isSuccessful()) {
                                Uri downloadUri = uriTask.getResult();
                                saveAdminData(firstName, lastName, email, mobile, downloadUri.toString());
                            } else {
                                progressDialog.dismiss();
                                Log.e(TAG, "Failed to get download URL: ", uriTask.getException());
                                Toast.makeText(AdminRegisterActivity.this, "Failed to get download URL. Try again!", Toast.LENGTH_LONG).show();
                            }
                        }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e(TAG, "Profile image upload failed: ", e);
                    Toast.makeText(AdminRegisterActivity.this, "Failed to upload profile image. Try again!", Toast.LENGTH_LONG).show();
                });
    }

    private void saveAdminData(String firstName, String lastName, String email, String mobile, String profileImageUrl) {
        Admin admin = new Admin(firstName, lastName, email, mobile, profileImageUrl);
        mDatabase.child(mAuth.getCurrentUser().getUid()).setValue(admin)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // Save admin details to SessionManager
                        sessionManager.setLoggedIn(true);
                        sessionManager.setAdminProfile(profileImageUrl, firstName + " " + lastName, email);

                        Toast.makeText(AdminRegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class));
                    } else {
                        Log.e(TAG, "Failed to save user data: ", task.getException());
                        Toast.makeText(AdminRegisterActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static class Admin {
        public String firstName, lastName, email, mobile, profileImageUrl;

        public Admin() {
        }

        public Admin(String firstName, String lastName, String email, String mobile, String profileImageUrl) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.mobile = mobile;
            this.profileImageUrl = profileImageUrl;
        }
    }
}
