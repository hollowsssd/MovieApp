package com.example.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Activities.AdminDetailUsersActivity;
import com.example.movieapp.Models.User;
import com.example.movieapp.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    private Context context;
    private ArrayList<User> UserList;
    private ArrayList<User> originalList;

    public UsersAdapter(Context context, ArrayList<User> UserList) {
        this.context = context;
        this.UserList = new ArrayList<>(UserList);
        this.originalList = new ArrayList<>(UserList);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.user_items, parent, false);
        return new UserHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = UserList.get(position);
        holder.tvName.setText(user.getDisplayName());
        holder.tvEmail.setText(user.getEmail());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminDetailUsersActivity.class);
            intent.putExtra("id", user.uid);
            intent.putExtra("displayName", user.displayName);
            intent.putExtra("isAdmin", user.isAdmin);

            intent.putExtra("email", user.email);


            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;


        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }


    public void filter(String text) {
        UserList.clear();
        if (text.isEmpty()) {
            UserList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (User user : originalList) {
                if (user.getDisplayName().toLowerCase().contains(text)) {
                    UserList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

}
