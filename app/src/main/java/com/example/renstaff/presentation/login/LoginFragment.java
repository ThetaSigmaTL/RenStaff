package com.example.renstaff.presentation.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renstaff.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextView registerNowButton;
    private TextView forgotPasswordButton;
    private Button loginButton;
    private Fragment registerFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        registerNowButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerNowButton:
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.loginFragmentContainer, registerFragment, "Register Fragment")
                        .commit();
                break;
            case R.id.forgotPasswordButton:
                Toast.makeText(getContext(), "Forgot password", Toast.LENGTH_SHORT).show();
                break;
            case R.id.loginButton:
                Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void init(View view) {
        registerNowButton = view.findViewById(R.id.registerNowButton);
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton);
        loginButton = view.findViewById(R.id.loginButton);
        registerFragment = new RegisterFragment();
    }
}