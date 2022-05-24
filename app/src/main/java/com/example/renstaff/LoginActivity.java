package com.example.renstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.renstaff.presentation.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Fragment loginFragment = new LoginFragment();
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mAuth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.loginFragmentContainer, loginFragment, "LoginFragment")
                    .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            Intent intent = new Intent(this, NavigationBottomActivity.class);
//            startActivity(intent);
//            finish();
//            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
//        } else {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.loginFragmentContainer, loginFragment, "LoginFragment")
//                    .commit();
//        }
    }
}