package com.example.renstaff.ui.chat;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.renstaff.data.utilities.Constants;
import com.example.renstaff.data.utilities.PreferenceManager;
import com.example.renstaff.databinding.FragmentChatBinding;
import com.example.renstaff.domain.RecentConversationAdapter;
import com.example.renstaff.domain.listeners.ChatListener;
import com.example.renstaff.models.MessageModel;
import com.example.renstaff.models.UserModel;
import com.example.renstaff.presentation.ChatActivity;
import com.example.renstaff.presentation.UsersActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment  implements ChatListener {


    private ChatViewModel chatViewModel;
    private FragmentChatBinding binding;
    private Intent intent;
    private List<MessageModel> messageModels;
    private RecentConversationAdapter conversationAdapter;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        intent = new Intent(getContext(), UsersActivity.class);
        binding.fabNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        binding.conversationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listenConversations();

        return root;
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    MessageModel messageModel = new MessageModel();
                    messageModel.senderId = senderId;
                    messageModel.receiverId = receiverId;
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                        messageModel.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        messageModel.conversationLastName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_LAST_NAME);
                        messageModel.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        messageModel.dateTime = getDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    } else {
                        messageModel.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        messageModel.conversationLastName = documentChange.getDocument().getString(Constants.KEY_SENDER_LAST_NAME);
                        messageModel.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        messageModel.dateTime = getDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    }
                    messageModel.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    messageModel.dataObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    messageModels.add(messageModel);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < messageModels.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (messageModels.get(i).senderId.equals(senderId) && messageModels.get(i).receiverId.equals(receiverId)) {
                            messageModels.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            messageModels.get(i).dateTime = getDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                            messageModels.get(i).dataObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(messageModels, (obj1, obj2) -> obj2.dataObject.compareTo(obj1.dataObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            if (messageModels.size() > 0) {
                binding.haventChat.setVisibility(View.GONE);
            } else {
                binding.haventChat.setVisibility(View.VISIBLE);
            }

        }
    };

    private String getDateTime(Date date) {
        return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(date);
    }

    private void listenConversations() {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private void init() {
        messageModels = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(messageModels, this::onChatClicked);
        binding.conversationsRecyclerView.setAdapter(conversationAdapter);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onChatClicked(UserModel user) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}