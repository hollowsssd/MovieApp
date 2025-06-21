package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.movieapp.Models.User;
import com.example.movieapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisActivity extends AppCompatActivity {
    EditText etEmail, etName, etPass, etCheckPass;
    TextView loginbtn;
    Button btnRes;

    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);

        // Ánh xạ các thành phần giao diện
        etEmail = findViewById(R.id.editEmailTxt);
        etName = findViewById(R.id.editNameTxt);
        etPass = findViewById(R.id.editPassText);
        etCheckPass = findViewById(R.id.editCheckPassText);
        btnRes = findViewById(R.id.addUserBtn);
        loginbtn =findViewById(R.id.loginRedirectTxt);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Gọi đăng ký (ví dụ)


        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                String checkpass = etCheckPass.getText().toString().trim();

                // KIỂM TRA RỖNG
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(checkpass)) {
                    Toast.makeText(RegisActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // KIỂM TRA EMAIL
                if (!isValidEmail(email)) {
                    Toast.makeText(RegisActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // KIỂM TRA MẬT KHẨU
                if (!password.equals(checkpass)) {
                    Toast.makeText(RegisActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<6){
                    Toast.makeText(RegisActivity.this, "Mật khẩu không dưới 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }




                registerUser(email, password, name, false);

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisActivity.this, LoginActivity.class));
            }
        });
    }




    public void registerUser(String email, String password, String name, boolean isAdmin) {
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
                                    Log.d("AUTH", "Đăng ký + lưu Firestore thành công");
                                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("AUTH", "Lưu Firestore lỗi: " + e.getMessage());
                                    Toast.makeText(this, "Lỗi lưu user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        // Đăng ký thất bại
                        Exception e = task.getException();
                        Log.e("AUTH", "Đăng ký lỗi: " + (e != null ? e.getMessage() : "unknown"));
                        Toast.makeText(this, "Email đã được dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // KIỂM TRA EMAIL
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && !email.startsWith(" ") && !email.endsWith(" ");
    }

}

