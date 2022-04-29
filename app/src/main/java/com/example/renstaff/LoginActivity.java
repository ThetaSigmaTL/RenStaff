package com.example.renstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.renstaff.presentation.login.LoginFragment;
import com.example.renstaff.presentation.login.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private Fragment loginFragment = new LoginFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.loginFragmentContainer, loginFragment, "LoginFragment")
                .commit();
    }
}