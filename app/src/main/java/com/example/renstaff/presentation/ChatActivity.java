package com.example.renstaff.presentation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.renstaff.MainActivity;
import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.example.renstaff.databinding.ActivityChatBinding;
import com.example.renstaff.domain.ChatAdapter;
import com.example.renstaff.models.MessageModel;
import com.example.renstaff.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends BaseActivity {

    private ActivityChatBinding binding;
    private UserModel receiverUserModel;
    private List<MessageModel> messageModels;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId = null;
    private Boolean isReceiverOnline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        clickListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenReceiverStatus();
    }

    private void clickListeners() {
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.inputMessage.getText().toString().isEmpty()){
                    sendMessage();
                }
            }
        });
        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    private void loadReceiverDetails() {
        receiverUserModel = (UserModel) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.nameLastNameTextView.setText(receiverUserModel.name+" "+ receiverUserModel.lastName);
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        messageModels = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageModels, preferenceManager.getString(Constants.KEY_USER_ID));
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUserModel.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if (conversionId != null) {
            updateConversion(binding.inputMessage.getText().toString());
        } else {
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            conversion.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_SENDER_LAST_NAME, preferenceManager.getString(Constants.KEY_LAST_NAME));
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUserModel.id);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverUserModel.name);
            conversion.put(Constants.KEY_RECEIVER_LAST_NAME, receiverUserModel.lastName);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
        binding.inputMessage.setText(null);
    }

    private void listenReceiverStatus() {
        database.collection(Constants.KEY_COLLECTION_USERS).document(receiverUserModel.id)
                .addSnapshotListener(ChatActivity.this, (value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        if (value.getLong(Constants.KEY_STATUS) != null) {
                            int status = Objects.requireNonNull(
                                    value.getLong(Constants.KEY_STATUS)).intValue();
                                    isReceiverOnline = status == 1;
                        }
                    }
                    if (isReceiverOnline) {
                        binding.statusTextView.setVisibility(View.VISIBLE);
                    } else {
                        binding.statusTextView.setVisibility(View.GONE);
                    }
                });
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUserModel.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUserModel.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = messageModels.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
               if (documentChange.getType() == DocumentChange.Type.ADDED){
                    MessageModel messageModel = new MessageModel();
                    messageModel.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    messageModel.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    messageModel.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    messageModel.dateTime = getDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    messageModel.dataObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    messageModels.add(messageModel);
               }
            }
            Collections.sort(messageModels, (obj1, obj2) -> obj1.dataObject.compareTo(obj2.dataObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeChanged(messageModels.size(), messageModels.size());
                binding.chatRecyclerView.smoothScrollToPosition(messageModels.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversionId == null) {
            checkForConversion();
        }
    };

    private String getDateTime(Date date) {
        return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message) {
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message, Constants.KEY_TIMESTAMP, new Date());
    }

    private void checkForConversion() {
        if (messageModels.size() != 0) {
            checkForConversionRemotely(preferenceManager.getString(Constants.KEY_USER_ID),
                    receiverUserModel.id);
            checkForConversionRemotely(receiverUserModel.id, preferenceManager
                    .getString(Constants.KEY_USER_ID));
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };

}