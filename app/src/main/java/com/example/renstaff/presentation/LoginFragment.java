package com.example.renstaff.presentation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renstaff.MainActivity;
import com.example.renstaff.R;
import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.example.renstaff.presentation.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.prefs.PreferenceChangeEvent;

public class LoginFragment extends Fragment implements View.OnClickListener {

    // объявление переменных
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
    private ProgressBar progressBar;
    PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        preferenceManager = new PreferenceManager(this.getActivity().getApplicationContext());
        registerNowButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        emailListener();
        passwordListener();

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
                loading(true);
                clearFocus();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                if (emailValid() && passwordValid()) {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                database.collection(Constants.KEY_COLLECTION_USERS)
                                        .whereEqualTo(Constants.KEY_EMAIL, emailEditText.getText().toString())
                                        .whereEqualTo(Constants.KEY_PASSWORD, passwordEditText.getText().toString())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful() && task1.getResult() != null
                                            && task1.getResult().getDocuments().size() > 0) {
                                                DocumentSnapshot documentSnapshot = task1.getResult().getDocuments().get(0);
                                                preferenceManager.putBoolean(Constants.KEY_SIGNED_IN, true);
                                                preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                                                preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                                                preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                                                preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                getToken();
                                                startActivity(intent);
                                                loading(false);
                                                getActivity().finish();
                                                Log.d("LoginF", preferenceManager.getString(Constants.KEY_USER_ID));
                                            }
                                        });

                            } else {
                                loading(false);
                                emailTextInputLayout.setError("Проверьте правильность электронной почты.");
                                passwordTextInputLayout.setError("Проверьте правильность пароля.");
                            }
                        }
                    });
                } else {
                    loading(false);
                    Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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


    // валидация поля Email при расфокусировке
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
    // валидация поля Password при расфокусировке
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

    // расфокусировка полей
    private void clearFocus() {
        emailTextInputLayout.clearFocus();
        passwordTextInputLayout.clearFocus();
    }
    // валидация поля Email
    private boolean emailValid() {
        if (emailEditText.getText().toString().isEmpty()) {
            emailTextInputLayout.setError("Поле не может быть пустым.");
            return false;
        }
        return true;
    }

    // валидация поля Password
    private boolean passwordValid() {
        if (passwordEditText.getText().toString().isEmpty()) {
            passwordTextInputLayout.setError("Поле не может быть пустым");
            return false;
        }
        return true;
    }

    // вкл/выкл прогресс бара
    private void loading(Boolean isLoading) {
        if (isLoading) {
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    // загрузка аватара
//    private String encodeImage(Bitmap bitmap) {
//        int previewWidth = 150;
//        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
//        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50,  byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        return Base64.getEncoder().encodeToString(bytes);
//    }

    // инициализация объектов
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
        progressBar = view.findViewById(R.id.progressBar);
    }
}