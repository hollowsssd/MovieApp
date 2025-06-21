package com.example.movieapp.Activities;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Adapters.MovieAdapter;
import com.example.movieapp.Adapters.UsersAdapter;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.Models.User;
import com.example.movieapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminListUsersActivity extends AppCompatActivity {
    Button btnthem;
    ImageButton btnBack1;
    RecyclerView recyclerView;

    private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_users);

        btnthem = findViewById(R.id.btnthem);
        btnBack1 = findViewById(R.id.btnBack1);
        recyclerView = findViewById(R.id.usersRecyclerView);

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminListUsersActivity.this, AdminAddUserActivity.class);
                startActivity(intent);

            }
        });
        btnBack1.setOnClickListener(v -> finish());

        loadUsers();

    }

    private void loadUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> UsersList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            UsersList.add(user);
                        }
                    }
                    setupRecyclerView(UsersList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Load user error", e));
    }

    private void setupRecyclerView(ArrayList<User> users) {
        usersAdapter = new UsersAdapter(this, users);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(usersAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }
}
