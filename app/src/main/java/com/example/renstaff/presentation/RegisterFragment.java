package com.example.renstaff.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.renstaff.MainActivity;
import com.example.renstaff.R;
import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.regex.Pattern;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    // объявление переменных
    private ImageView backButton;
    private Fragment loginFragment;
    private Button registerButton;
    private ProgressBar progressBar;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout,
            confirmPasswordTextInputLayout, nameTextInputLayout, lastNameTextInputLayout;
    private TextInputEditText nameEditText, lastNameEditText, emailEditText,
            passwordEditText, confirmPasswordEditText;
    private FirebaseAuth mAuth;
    private int minPasswordLength;
    private String emptyField = "Поле не может быть пустым.";
    private Intent intent;
    private PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        validationAllFields();
        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return view;
    }

    // обработчик нажатий на кнопки
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.loginFragmentContainer, loginFragment, "Login fragment")
                        .commit();
                break;
            case R.id.registerButton:
                loading(true);
                clearFocus();
                if (checkEditTexts()) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                            passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                HashMap<String, Object> user = new HashMap<>();
                                user.put(Constants.KEY_NAME, nameEditText.getText().toString());
                                user.put(Constants.KEY_LAST_NAME, lastNameEditText.getText().toString());
                                user.put(Constants.KEY_EMAIL, emailEditText.getText().toString());
                                user.put(Constants.KEY_PASSWORD, passwordEditText.getText().toString());
                                user.put(Constants.KEY_USER_AUTH_ID, mAuth.getCurrentUser().getUid());
                                database.collection(Constants.KEY_COLLECTION_USERS)
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> {
                                            preferenceManager.putBoolean(Constants.KEY_SIGNED_IN, true);
                                            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                            preferenceManager.putString(Constants.KEY_EMAIL, emailEditText.getText().toString());
                                            preferenceManager.putString(Constants.KEY_NAME, nameEditText.getText().toString());
                                            preferenceManager.putString(Constants.KEY_LAST_NAME, lastNameEditText.getText().toString());
                                            getToken();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            Log.d("DocRef", documentReference.getId());
                                        }).addOnFailureListener(exception -> {
                                    loading(false);
                                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                loading(false);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                loading(false);
                                Toast.makeText(getContext(), "UserModel are not created", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    loading(false);
                    Log.d("RegistrationButton", "Error");
                }
                break;
        }
    }

    // валидация всех полей
    private void validationAllFields() {
        nameValid();
        lastNameValid();
        emailValid();
        passwordValid();
        confirmPasswordValid();
    }

    // инициализация объектов
    private void init(View view) {
        progressBar = view.findViewById(R.id.progressBar);
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
        nameTextInputLayout = view.findViewById(R.id.nameTextInputLayout);
        nameEditText = view.findViewById(R.id.nameEditText);
        lastNameTextInputLayout = view.findViewById(R.id.lastNameTextInputLayout);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        mAuth = FirebaseAuth.getInstance();
        intent = new Intent(getContext(), MainActivity.class);
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
    }

    // проверка полей
    private boolean checkEditTexts() {
        if (nameEditText.getText().toString().isEmpty()) {
            nameTextInputLayout.setError(emptyField);
            return false;
        } else if (!namePattern()) {
            nameTextInputLayout.setError("Поле не может содержать пробелы.");
            return false;
        } else if (lastNameEditText.getText().toString().isEmpty()) {
            lastNameTextInputLayout.setError(emptyField);
            return false;
        } else if (!lastNamePattern()) {
            lastNameTextInputLayout.setError("Поле не может содержать пробелы.");
            return false;
        } else if (emailEditText.getText().toString().isEmpty()) {
            emailTextInputLayout.setError(emptyField);
            return false;
        } else if (!emailPattern()) {
            emailTextInputLayout.setError("Неверный формат email.");
            return false;
        } else if (passwordEditText.getText().toString().isEmpty()) {
            passwordTextInputLayout.setError(emptyField);
            return false;
        } else if (passwordEditText.getText().toString().length() < minPasswordLength) {
            passwordTextInputLayout.setError("Пароль должен содержать 8 символов.");
            return false;
        } else if (confirmPasswordEditText.getText().toString().isEmpty()) {
            confirmPasswordTextInputLayout.setError(emptyField);
            return false;
        } else if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            confirmPasswordTextInputLayout.setError("Пароли не совпадают.");
            return false;
        }
        return true;
    }

    // расфокусировка
    private void clearFocus() {
        confirmPasswordTextInputLayout.clearFocus();
        passwordTextInputLayout.clearFocus();
        emailTextInputLayout.clearFocus();
        nameTextInputLayout.clearFocus();
        lastNameTextInputLayout.clearFocus();
    }

    // валидация поля Name
    private void nameValid() {
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus) {
                    if (nameEditText.getText().toString().isEmpty()) {
                        nameTextInputLayout.setError(emptyField);
                    } else if (!namePattern()) {
                        nameTextInputLayout.setError("Поле не может содержать пробелы.");
                    }
                }
            }
        });
    }

    // валидация поля Last name
    private void lastNameValid() {
        lastNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastNameTextInputLayout.setErrorEnabled(false);
                }
                if (!hasFocus) {
                    if (lastNameEditText.getText().toString().isEmpty()) {
                        lastNameTextInputLayout.setError(emptyField);
                    } else if (!lastNamePattern()) {
                        lastNameTextInputLayout.setError("Поле не может содержать пробелы.");
                    }
                }
            }
        });
    }

    // валидация поля Email
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

    // валидация поля  Password
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

    // валидация поля Confirm password
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

    // паттерн проверки поля Name
    private boolean namePattern() {
        Pattern pattern = Pattern.compile("\\s");
        if (pattern.matcher(nameEditText.getText().toString()).find()) {
            return false;
        }
        return true;
    }

    // паттерн проверки поля Last name
    private boolean lastNamePattern() {
        Pattern pattern = Pattern.compile("\\s");
        if (pattern.matcher(lastNameEditText.getText().toString()).find()) {
            return false;
        }
        return true;
    }

    // паттерн проверки поля Email
    private boolean emailPattern() {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            return false;
        }
        return true;
    }

    // паттерн проверки поля Password
    private boolean passwordPattern() {
        String passwordText = passwordEditText.getText().toString();
        if (passwordText.length() < minPasswordLength) {
            return false;
        }
        return true;
    }

    // проверка поля Confirm password на совпадение с полем Password
    private boolean comparePassword() {
        if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
            return false;
        }
        return true;
    }

    // вкл/выкл прогресс бара
    private void loading(Boolean isLoading) {
        if (isLoading) {
            registerButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.VISIBLE);
        }
    }

    // добавление данных в базу данных Firebase и в SharedPreference
    private void addDataToFirestore() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, nameEditText.getText().toString());
        user.put(Constants.KEY_LAST_NAME, lastNameEditText.getText().toString());
        user.put(Constants.KEY_EMAIL, emailEditText.getText().toString());
        user.put(Constants.KEY_PASSWORD, passwordEditText.getText().toString());
        user.put(Constants.KEY_USER_AUTH_ID, mAuth.getCurrentUser().getUid());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_EMAIL, emailEditText.getText().toString());
                    preferenceManager.putString(Constants.KEY_NAME, nameEditText.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, lastNameEditText.getText().toString());
                    Log.d("DocRef", documentReference.getId());
                }).addOnFailureListener(exception -> {
            loading(false);
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> Log.d("FCM Update", token))
                .addOnFailureListener(e -> Log.d("FCM Update Failed", token));
    }

}