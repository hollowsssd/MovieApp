package com.example.movieapp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        Button getinBtn=findViewById(R.id.getInBtn);

        db = FirebaseFirestore.getInstance();

        getinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null ) {
                    db.collection("users").document(firebaseUser.getUid())
                            .get()
                            .addOnSuccessListener(document -> {
                                if (document.exists()) {
                                    boolean isAdmin = document.getBoolean("isAdmin") != null && document.getBoolean("isAdmin");
                                    String name = document.getString("displayName");
                                    if (isAdmin) {
                                        Toast.makeText(v.getContext(), "Welcome back ! "+name, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(IntroActivity.this, AdminActivity.class));
                                    } else {
                                        Toast.makeText(v.getContext(), "Welcome back ! "+name, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                    }
                                }
                            });

                } else {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class)); // Chưa đăng nhập → Hiện màn login
                }
                }
        });


    }
}