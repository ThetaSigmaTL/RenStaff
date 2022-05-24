package com.example.renstaff.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.renstaff.LoginActivity;
import com.example.renstaff.MainActivity;
import com.example.renstaff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private Fragment loginFragment = new LoginFragment();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Intent intent;
                if (currentUser != null) {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);

        Log.d("SplashSc", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("SplashSc", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SplashSc", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SplashSc", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SplashSc", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SplashSc", "onDestroy");
    }
}