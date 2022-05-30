package com.example.renstaff.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.example.renstaff.databinding.ActivityUsersBinding;
import com.example.renstaff.domain.UsersAdapter;
import com.example.renstaff.domain.listeners.UserListener;
import com.example.renstaff.models.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUsers();
        binding.backImageView.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers() {
        progressBar(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                        progressBar(false);
                        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<UserModel> userModels = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                    continue;
                                };
                                UserModel userModel = new UserModel();
                                userModel.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                                userModel.lastName = queryDocumentSnapshot.getString(Constants.KEY_LAST_NAME);
                                userModel.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                                userModel.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                userModel.id = queryDocumentSnapshot.getId();
                                userModels.add(userModel);
                            }
                            if (userModels.size() > 0) {
                                UsersAdapter usersAdapter = new UsersAdapter(userModels, this);
                                binding.usersRecyclerView.setAdapter(usersAdapter);
                                binding.usersRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                showErrorMessage();
                            }
                        } else {
                            showErrorMessage();
                        }
                });
    }

    private void showErrorMessage() {
        binding.errorMessageTextView.setText(String.format("$s", "No user available"));
    }

    private void progressBar (Boolean visible) {
        if (visible) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(UserModel userModel) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, userModel);
        startActivity(intent);
        finish();
    }
}