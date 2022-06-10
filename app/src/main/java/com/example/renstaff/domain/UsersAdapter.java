package com.example.renstaff.domain;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renstaff.databinding.ItemAddUserBinding;
import com.example.renstaff.domain.listeners.UserListener;
import com.example.renstaff.models.UserModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<UserModel> userModels;
    private final UserListener userListener;

    public UsersAdapter(List<UserModel> userModels, UserListener userListener) {
        this.userModels = userModels;
        this.userListener = userListener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddUserBinding itemAddUserBinding = ItemAddUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(itemAddUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(userModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private ItemAddUserBinding binding;

        public UserViewHolder(@NonNull ItemAddUserBinding itemAddUserBinding) {
            super(itemAddUserBinding.getRoot());
            binding = itemAddUserBinding;
        }

        void setUserData(UserModel userModel) {
            binding.nameTextView.setText(userModel.name+" "+ userModel.lastName);
            binding.emailTextView.setText(userModel.email);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(userModel));
        }
    }

}
