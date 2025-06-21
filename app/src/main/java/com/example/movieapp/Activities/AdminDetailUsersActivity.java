package com.example.movieapp.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminDetailUsersActivity extends AppCompatActivity {

    TextView editEmailTxt, editNameTxt;
    ImageButton btnback;
    Button btnSubmit;
    CheckBox checkBox;

    ProgressBar progressBar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_user);

        editEmailTxt = findViewById(R.id.editEmailTxt);
        editNameTxt = findViewById(R.id.editNameTxt);
        checkBox = findViewById(R.id.isAdmin);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnback=findViewById(R.id.btnBack1);
        progressBar = findViewById(R.id.progressBar);
        db= FirebaseFirestore.getInstance();

        btnback.setOnClickListener(V->finish());

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("displayName");
        boolean isAdmin = intent.getBooleanExtra("isAdmin", false);


        editEmailTxt.setText(email);
        editNameTxt.setText(name);
        checkBox.setChecked(isAdmin);


        btnSubmit.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            String newName = editNameTxt.getText().toString().trim();
            boolean IsAdmin = checkBox.isChecked();

            if (TextUtils.isEmpty(name)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("displayName", newName);
            userUpdates.put("isAdmin", IsAdmin);

            db.collection("users").document(id)
                    .update(userUpdates)
                    .addOnSuccessListener(aVoid -> {
                        progressBar.setVisibility(View.GONE);
                        Log.d("TAG", "User updated in Firestore");
                        Toast.makeText(AdminDetailUsersActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Log.e("TAG", "Lỗi cập nhật Firestore", e);
                        Toast.makeText(AdminDetailUsersActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });




    }
}
