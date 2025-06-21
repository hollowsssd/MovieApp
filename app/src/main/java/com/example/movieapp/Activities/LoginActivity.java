package com.example.movieapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.movieapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdt, passEdt;

    private Button loginBtn;

    private TextView resBtn;

    private CheckBox checkBox;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        emailEdt = findViewById(R.id.editNameTxt);
        passEdt = findViewById(R.id.editPassText);
        loginBtn = findViewById(R.id.loginBtn);
        resBtn = findViewById(R.id.resRedirectTxt);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        checkBox = findViewById(R.id.rememberMeCheckbox);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString().trim();
                String password = passEdt.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);
            }
        });

        resBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisActivity.class));
            }
        });
    }


    public void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();

                        // Lấy user từ Firestore
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String name = documentSnapshot.getString("displayName");
                                        String emailTK= documentSnapshot.getString("email");
                                        boolean isAdmin = documentSnapshot.getBoolean("isAdmin") != null &&
                                                documentSnapshot.getBoolean("isAdmin");

                                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putBoolean("rememberMe", checkBox.isChecked());
                                        editor.apply();

                                        if (!checkBox.isChecked()) {
                                            FirebaseAuth.getInstance().signOut();
                                        }


                                        progressBar.setVisibility(View.GONE);
                                        // Đăng nhập thành công + lấy thông tin user
                                        Toast.makeText(this,
                                                "Đăng nhập thành công!\nTên: " + name + "\nAdmin: " + isAdmin,
                                                Toast.LENGTH_SHORT).show();

                                        if (isAdmin){
                                            Intent intent = new Intent(this, AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                            }else {
                                                // Chuyển màn hình sau khi đăng nhập thành công
                                                Intent intent = new Intent(this, MainActivity.class);
                                                intent.putExtra("name", name);  // Có thể chuyền thêm dữ liệu nếu cần\
                                                intent.putExtra("email",emailTK);
                                                startActivity(intent);
                                                finish();  // Đóng màn hình đăng nhập
                                            }
                                    } else {
                                        Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Lỗi khi đọc Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && !email.startsWith(" ") && !email.endsWith(" ");
    }

}
