package com.example.renstaff.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renstaff.MainActivity;
import com.example.renstaff.R;
import com.example.renstaff.presentation.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextView registerNowButton;
    private TextView forgotPasswordButton;
    private Button loginButton;
    private Fragment registerFragment;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        registerNowButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        emailListener();
        passwordListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Login fragment destroyed", Toast.LENGTH_SHORT).show();
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
                clearFocus();
                if (emailValid() && passwordValid()) {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                            } else {
                                emailTextInputLayout.setError("Проверьте правильность электронной почты.");
                                passwordTextInputLayout.setError("Проверьте правильность пароля.");
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void emailListener() {
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus) {
                    emailValid();
                }
            }
        });
    }

    private void passwordListener() {
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passwordTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus) {
                    passwordValid();
                }
            }
        });
    }

    private void clearFocus() {
        emailTextInputLayout.clearFocus();
        passwordTextInputLayout.clearFocus();
    }

    private boolean emailValid() {
        if (emailEditText.getText().toString().isEmpty()) {
            emailTextInputLayout.setError("Поле не может быть пустым.");
            return false;
        }
        return true;
    }

    private boolean passwordValid() {
        if (passwordEditText.getText().toString().isEmpty()) {
            passwordTextInputLayout.setError("Поле не может быть пустым");
            return false;
        }
        return true;
    }

    private void init(View view) {
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        registerNowButton = view.findViewById(R.id.registerNowButton);
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton);
        loginButton = view.findViewById(R.id.loginButton);
        registerFragment = new RegisterFragment();
        mAuth = FirebaseAuth.getInstance();
        intent = new Intent(getContext(), MainActivity.class);
    }
}