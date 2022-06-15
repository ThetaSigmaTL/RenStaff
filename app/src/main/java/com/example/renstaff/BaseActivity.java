package com.example.renstaff;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {

    DocumentReference documentReference;
    FirebaseFirestore database;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));

    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_STATUS, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("prefmen", preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_STATUS, 1);
    }

}
