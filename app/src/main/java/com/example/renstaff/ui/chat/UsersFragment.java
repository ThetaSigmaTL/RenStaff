package com.example.renstaff.ui.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.example.renstaff.databinding.FragmentUsersBinding;
import com.example.renstaff.domain.UsersAdapter;
import com.example.renstaff.domain.listeners.UserListener;
import com.example.renstaff.models.UserModel;
import com.example.renstaff.ui.chat.ChatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment implements UserListener {

    private PreferenceManager preferenceManager;
    private FragmentUsersBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        getUsers();
        binding.backImageView.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        View root = binding.getRoot();
        return root;
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
        Intent intent = new Intent(requireActivity()
                .getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, userModel);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}