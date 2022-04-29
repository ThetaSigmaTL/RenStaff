package com.example.renstaff.presentation.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.renstaff.R;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private ImageView backButton;
    private Fragment loginFragment;
    private Button registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.loginFragmentContainer, loginFragment, "Login fragment")
                        .commit();
                break;
            case R.id.registerButton:
                Toast.makeText(getContext(), "Register button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void init(View view) {
        backButton = view.findViewById(R.id.backButton);
        loginFragment = new LoginFragment();
        registerButton = view.findViewById(R.id.registerButton);
    }
}