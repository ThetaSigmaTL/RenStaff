package com.example.renstaff.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.renstaff.LoginActivity;
import com.example.renstaff.R;
import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.example.renstaff.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private FirebaseAuth mAuth;
    private Button signOut;
    private Intent intent;
    private PreferenceManager preferenceManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        intent = new Intent(getContext(), LoginActivity.class);
        userInfo();

        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference = database
                        .collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID));
                HashMap<String, Object> updates = new HashMap<>();
                updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                documentReference.update(updates);
                preferenceManager.clear();
                startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }

    private void setClickListeners() {

        // выход из аккаунта

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void userInfo() {
        String nameLastName = preferenceManager.getString(Constants.KEY_NAME) +" "
                + preferenceManager.getString(Constants.KEY_LAST_NAME);
        binding.nameAndLastName.setText(nameLastName);
        binding.email.setText(preferenceManager.getString(Constants.KEY_EMAIL));
    }

    private void onClickChooseImage() {

    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
    }
}