package com.example.renstaff.domain;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renstaff.databinding.ItemRecievedMessageBinding;
import com.example.renstaff.databinding.ItemSentMessageBinding;
import com.example.renstaff.models.MessageModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MessageModel> messageModels;
    private final String senderId;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<MessageModel> messageModels, String senderId) {
        this.messageModels = messageModels;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemSentMessageBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent, false));
        } else {
            return new ReceivedMessageViewHolder(
                    ItemRecievedMessageBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(messageModels.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(messageModels.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemSentMessageBinding binding;

        public SentMessageViewHolder(ItemSentMessageBinding itemSentMessageBinding) {
            super(itemSentMessageBinding.getRoot());
            binding = itemSentMessageBinding;
        }

        void setData(MessageModel messageModel) {
            binding.textMessageTextView.setText(messageModel.message);
            binding.dateTimeTextView.setText(messageModel.dateTime);
            Log.d("setSentData", "ok");
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecievedMessageBinding binding;


        public ReceivedMessageViewHolder(ItemRecievedMessageBinding itemRecievedMessageBinding) {
            super(itemRecievedMessageBinding.getRoot());
            binding = itemRecievedMessageBinding;
        }
        void setData(MessageModel messageModel) {
            binding.textMessageTextView.setText(messageModel.message);
            binding.dateTimeTextView.setText(messageModel.dateTime);
            Log.d("setReceiveData", "ok");
        }
    }
}
