package com.example.movieapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.movieapp.R;
import com.example.movieapp.Activities.IntroActivity;

public class SplashActivity extends Activity {

    private ImageView logo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Đặt setContentView trước

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // Tắt splash screen mặc định của Android 12+
            // Đảm bảo rằng bạn không cần thiết lập lại SplashScreen mặc định của hệ thống
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        logo = findViewById(R.id.logo_image);
        progressBar = findViewById(R.id.loading_spinner);

        // Load animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn); // Hiển thị logo với hiệu ứng fade-in

        // Delay 2.5 giây rồi chuyển màn hình
        new Handler().postDelayed(() -> {
            // Ẩn logo và hiển thị ProgressBar
            logo.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // Chuyển màn hình sau 1.5 giây
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }, 1000); // Sau khi ProgressBar hiển thị 1 giây thì chuyển sang màn hình mới
        }, 2500); // Delay 2.5 giây để hiển thị logo
    }
}
