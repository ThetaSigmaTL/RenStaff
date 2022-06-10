package com.example.renstaff.domain;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renstaff.databinding.ItemUserChatBinding;
import com.example.renstaff.domain.listeners.ChatListener;
import com.example.renstaff.models.MessageModel;
import com.example.renstaff.models.UserModel;
import com.google.firebase.firestore.SnapshotMetadata;

import java.util.List;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder>{

    private final List<MessageModel> messageModels;
    private final ChatListener chatListener;

    public RecentConversationAdapter(List<MessageModel> messageModels, ChatListener chatListener) {
        this.messageModels = messageModels;
        this.chatListener = chatListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemUserChatBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(messageModels.get(position));
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    class ConversionViewHolder extends RecyclerView.ViewHolder {

        ItemUserChatBinding binding;

        public ConversionViewHolder(ItemUserChatBinding itemUserChatBinding) {
            super(itemUserChatBinding.getRoot());
            binding = itemUserChatBinding;
        }

        void setData(MessageModel messageModel) {
            binding.nameLastNameTextView.setText(messageModel.conversionName+" "+messageModel.conversationLastName);
            binding.textRecentMessage.setText(messageModel.message);
            binding.timeTextView.setText(messageModel.dateTime);
            binding.getRoot().setOnClickListener(v -> {
                UserModel user = new UserModel();
                user.id = messageModel.conversionId;
                user.name = messageModel.conversionName;
                user.lastName = messageModel.conversationLastName;
                chatListener.onChatClicked(user);
            });
        }
    }

}
