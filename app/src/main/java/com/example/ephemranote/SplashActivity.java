package com.example.ephemranote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final String prefsName = "MyPrefsFile";
    private static final String prefsFTime = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean(prefsFTime, true);

        new Handler().postDelayed(() -> {
            if (isFirstTime) {
                startActivity(new Intent(SplashActivity.this, CreateAccountActivity.class));
            } else {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
            finish();
        }, 1000);

        if (isFirstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(prefsFTime, false);
            editor.apply();
        }
    }
}