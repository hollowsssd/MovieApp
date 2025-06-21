package com.example.movieapp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.movieapp.Http.domain;
import com.example.movieapp.R;

public class MediaActivity extends AppCompatActivity {
    ImageView btnback, ic_baseline_rewind_10, pause_button,ic_baseline_fast_forward_10s,ic_subtitle;
    TextView tvTitle;
    PlayerView playerView;
    ExoPlayer exoPlayer;



    String url=domain.Url;
    String api_key=domain.api_key;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        String idVideo = intent.getStringExtra("videoUrl");
        String title = intent.getStringExtra("title");


        String videoUrl=url+"/emby/Videos/"+idVideo+"/stream?Static=true&api_key="+api_key;
        setContentView(R.layout.exo_player_control_view); // trỏ đúng tên file XML



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        playerView = findViewById(R.id.player_view);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer (exoPlayer);
        MediaItem mediaItem= MediaItem.fromUri(videoUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.play();

        btnback=playerView.findViewById(R.id.btnback);
        ic_baseline_rewind_10=playerView.findViewById(R.id.ic_baseline_rewind_10);
        pause_button=playerView.findViewById(R.id.pause_button);
        ic_baseline_fast_forward_10s=playerView.findViewById(R.id.ic_baseline_fast_forward_10s);
        ic_subtitle=playerView.findViewById(R.id.ic_subtitle);
        tvTitle=playerView.findViewById(R.id.Title);

        tvTitle.setText(title);

        pause_button.setOnClickListener(v -> {
            exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
            pause_button.setImageResource(Boolean.TRUE.equals(exoPlayer.getPlayWhenReady()) ? R.drawable.pause_button : R.drawable.iconplay);
        });

        ic_baseline_fast_forward_10s.setOnClickListener(v->{
            exoPlayer.seekTo(exoPlayer.getCurrentPosition()+10000);
        });

        ic_baseline_rewind_10.setOnClickListener(v->{
           long num = exoPlayer.getCurrentPosition() - 10000;
           if(num<0){
               exoPlayer.seekTo(0);
           }else {
               exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 10000);
           }

        });

        btnback.setOnClickListener(v->{
            finish();
        });

    }
    @Override
    protected void onResume(){
        exoPlayer.play();
        super.onResume();
    }

    @Override
    protected void onPause(){
        exoPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
    }




}
