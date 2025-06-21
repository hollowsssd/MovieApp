package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
    }

    // Phương thức chuyển đến trang người dùng
    public void goToUserPage(View v) {
        Intent intent = new Intent(AdminActivity.this, AdminListUsersActivity.class);
        startActivity(intent);
    }


    // Phương thức chuyển đến trang quản lý phim
    public void goToCinePage(View v) {
        Intent intent = new Intent(AdminActivity.this, AdminListMoviesActivity.class);
        startActivity(intent);
    }

    // Phương thức chuyển đến trang diễn viên
    public void ChangeInformation(View v) {
        Intent intent = new Intent(AdminActivity.this, ManageAccountActivity.class);
        startActivity(intent);
    }

    // Phương thức chuyển đến trang banner


    // Phương thức đăng xuất
    public void LogOut(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Kết thúc AdminActivity
    }
}
