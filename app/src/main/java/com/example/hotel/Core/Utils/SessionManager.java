package com.example.hotel.Core.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ADMIN_PROFILE_PHOTO = "adminProfilePhoto";
    private static final String KEY_ADMIN_FULL_NAME = "adminFullName";
    private static final String KEY_ADMIN_EMAIL = "adminEmail";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setAdminProfile(String profilePhoto, String fullName, String email) {
        editor.putString(KEY_ADMIN_PROFILE_PHOTO, profilePhoto);
        editor.putString(KEY_ADMIN_FULL_NAME, fullName);
        editor.putString(KEY_ADMIN_EMAIL, email);
        editor.apply();
    }

    public String getAdminProfilePhoto() {
        return sharedPreferences.getString(KEY_ADMIN_PROFILE_PHOTO, null);
    }

    public String getAdminFullName() {
        return sharedPreferences.getString(KEY_ADMIN_FULL_NAME, null);
    }

    public String getAdminEmail() {
        return sharedPreferences.getString(KEY_ADMIN_EMAIL, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
