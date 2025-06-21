package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.Models.User;
import com.example.movieapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class AdminAddUserActivity extends AppCompatActivity {
    EditText etEmail, etName, etPass, etCheckPass;

    Button btnSubmit ;
    CheckBox checkBox;
    ImageButton btnBack;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        etEmail = findViewById(R.id.editEmailTxt);
        etName = findViewById(R.id.editNameTxt);
        etPass = findViewById(R.id.editPassText);
        etCheckPass = findViewById(R.id.editCheckPassText);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack1);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);
        checkBox=findViewById(R.id.isAdmin);

        btnBack.setOnClickListener(V-> finish());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(!check()){
                    progressBar.setVisibility(View.GONE);
                    return;};

                String email = etEmail.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                boolean isAdmin= checkBox.isChecked();


                addUser(email, password, name, isAdmin);


            }

        });
    }
    public void addUser(String email, String password, String name, boolean isAdmin) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();

                        User user = new User(uid, email, name, isAdmin);

                        // Lưu vào Firestore
                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("AUTH", "Đăng ký + lưu Firestore thành công");
                                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    Log.e("AUTH", "Lưu Firestore lỗi: " + e.getMessage());
                                    Toast.makeText(this, "Lỗi lưu user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        // Đăng ký thất bại
                        progressBar.setVisibility(View.GONE);
                        Exception e = task.getException();
                        Log.e("AUTH", "Đăng ký lỗi: " + (e != null ? e.getMessage() : "unknown"));
                        Toast.makeText(this, "Email đã được dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private boolean check() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String checkpass = etCheckPass.getText().toString().trim();

        // KIỂM TRA RỖNG
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(checkpass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        // KIỂM TRA EMAIL
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // KIỂM TRA MẬT KHẨU
        if (!password.equals(checkpass)) {
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length()<6){
            Toast.makeText(this, "Mật khẩu không dưới 6 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && !email.startsWith(" ") && !email.endsWith(" ");
    }



}
