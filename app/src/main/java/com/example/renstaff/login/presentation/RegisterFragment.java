package com.example.renstaff.login.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.renstaff.NavigationBottomActivity;
import com.example.renstaff.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private ImageView backButton;
    private Fragment loginFragment;
    private Button registerButton;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout,
            confirmPasswordTextInputLayout, nicknameTextInputLayout;
    private TextInputEditText nicknameEditText, emailEditText,
            passwordEditText, confirmPasswordEditText;
    private FirebaseAuth mAuth;
    private int minPasswordLength;
    private String emptyField = "Поле не может быть пустым.";
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        nicknameValid();
        emailValid();
        passwordValid();
        confirmPasswordValid();
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
                clearFocus();
                if (checkEditTexts()) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                            passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "User are not created", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void init(View view) {
        backButton = view.findViewById(R.id.backButton);
        loginFragment = new LoginFragment();
        registerButton = view.findViewById(R.id.registerButton);
        minPasswordLength = 8;
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirmPasswordInputLayout);
        nicknameTextInputLayout = view.findViewById(R.id.nicknameTextInputLayout);
        nicknameEditText = view.findViewById(R.id.nicknameEditText);
        mAuth = FirebaseAuth.getInstance();
        intent = new Intent(getContext(), NavigationBottomActivity.class);
    }

    private boolean checkEditTexts() {
        if (nicknameEditText.getText().toString().isEmpty()) {
            nicknameTextInputLayout.setError(emptyField);
            return false;
        } else if (!nicknamePattern()) {
            nicknameTextInputLayout.setError("Никнейм не может содержать пробелы.");
            return false;
        } else if (emailEditText.getText().toString().isEmpty()) {
            emailTextInputLayout.setError(emptyField);
            return false;
        } else if(!emailPattern()) {
            emailTextInputLayout.setError("Неверный формат email.");
            return false;
        } else if (passwordEditText.getText().toString().isEmpty()) {
            passwordTextInputLayout.setError(emptyField);
            return false;
        } else if (passwordEditText.getText().toString().length() < minPasswordLength) {
            passwordTextInputLayout.setError("Пароль должен содержать 8 символов.");
            return false;
        } else if (confirmPasswordEditText.getText().toString().isEmpty()){
            confirmPasswordTextInputLayout.setError(emptyField);
            return false;
        } else if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            confirmPasswordTextInputLayout.setError("Пароли не совпадают.");
            return false;
        }
        return true;
    }

    private void clearFocus() {
        confirmPasswordTextInputLayout.clearFocus();
        passwordTextInputLayout.clearFocus();
        emailTextInputLayout.clearFocus();
        nicknameTextInputLayout.clearFocus();
    }

    private void nicknameValid() {
        nicknameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nicknameTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus ) {
                    if (nicknameEditText.getText().toString().isEmpty()) {
                        nicknameTextInputLayout.setError(emptyField);
                    } else if (!nicknamePattern()) {
                        nicknameTextInputLayout.setError("Никнейм не может содержать пробелы.");
                    }
                }
            }
        });
    }

    private void emailValid() {
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailTextInputLayout.setErrorEnabled(false);
                }

                if (!hasFocus) {
                    if (emailEditText.getText().toString().isEmpty()) {
                        emailTextInputLayout.setError(emptyField);
                    } else if (!emailPattern()) {
                        emailTextInputLayout.setError("Неверный формат email.");
                    }
                }
            }
        });
    }


    private void passwordValid() {
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passwordTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus) {
                    if (passwordEditText.getText().toString().isEmpty()) {
                        passwordTextInputLayout.setError(emptyField);
                    } else if (!passwordPattern()) {
                        passwordTextInputLayout.setError("Пароль должен содержать не менее 8 символов.");
                    }
                }
            }
        });
    }

    private void confirmPasswordValid() {
        confirmPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    confirmPasswordTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus && confirmPasswordEditText.getText().toString().isEmpty()) {
                    confirmPasswordTextInputLayout.setError("Поле не может быть пустым.");
                } else if (!hasFocus && !comparePassword()) {
                    confirmPasswordTextInputLayout.setError("Пароли не совпадают.");
                }
            }
        });
    }

    private boolean nicknamePattern() {
        Pattern pattern = Pattern.compile("\\s");
        if (pattern.matcher(nicknameEditText.getText().toString()).find()) {
            return false;
        }
        return true;
    }

    private boolean emailPattern() {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            return false;
        }
        return true;
    }

    private boolean passwordPattern() {
        String passwordText = passwordEditText.getText().toString();
        if (passwordText.length() < minPasswordLength) {
            return false;
        }
        return true;
    }

    private boolean comparePassword() {
        if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
            return false;
        }
        return true;
    }

}