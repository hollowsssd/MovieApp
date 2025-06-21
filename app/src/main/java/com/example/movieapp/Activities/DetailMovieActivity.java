package com.example.movieapp.Activities;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailMovieActivity extends AppCompatActivity {
    ImageView posterImage,btnFavorite;
    TextView titleText,tvRatting,tvYear,tvAge,tvDescription;

    Button btnplaynow;

    ImageButton btnBack;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailmovie);
        EdgeToEdge.enable(this);
        //ánh xạ
        posterImage = findViewById(R.id.posterImage);
        titleText = findViewById(R.id.titleText);
        btnBack = findViewById(R.id.btnBack1);
        tvYear = findViewById(R.id.yearText);
        tvRatting =findViewById(R.id.ratingText);
        tvAge=findViewById(R.id.ageText);
        tvDescription=findViewById(R.id.descriptionText);
        btnFavorite=findViewById(R.id.btnFavorite);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        String uid= mAuth.getCurrentUser().getUid();
        btnplaynow=findViewById(R.id.btnplaynow);


        Intent intent = getIntent();
        String idMovie = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int year = intent.getIntExtra("year", 0);
        int age = intent.getIntExtra("age", 0);
        ArrayList<String> genres = intent.getStringArrayListExtra("genres");
        double rating = intent.getDoubleExtra("rating", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");
        String videoUrl = intent.getStringExtra("videoUrl");

        titleText.setText(title);
        tvDescription.setText(description);
        tvYear.setText(String.valueOf(year));
        tvAge.setText(age + "+");
        tvRatting.setText(String.valueOf(rating));

        db.collection("favorites")
                .document(uid)
                .collection("movies")
                .document(idMovie)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Đã yêu thích → đổi icon
                        btnFavorite.setImageResource(R.drawable.favorite);
                    } else {
                        btnFavorite.setImageResource(R.drawable.nofavorite);
                    }
                });



        Glide.with(this)
                .load(imageUrl)
                .into(posterImage);


        btnBack.setOnClickListener(v -> finish());

        btnplaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DetailMovieActivity.this, MediaActivity.class);
                intent.putExtra("videoUrl",videoUrl);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });

        FlexboxLayout genreContainer = findViewById(R.id.genreContainer);

        for (String genre : genres) {
            TextView tv = new TextView(this);
            tv.setText(genre);
            tv.setTextSize(12f);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(dpToPx(8), dpToPx(2), dpToPx(8), dpToPx(2)); // giống XML kia
//            tv.setBackgroundResource(R.drawable.stroke_bg); // bo góc, màu nền nhẹ nếu muốn

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            tv.setLayoutParams(params);

            genreContainer.addView(tv);
        }
        //view more
        TextView tvDescription = findViewById(R.id.descriptionText);
        TextView seeMoreBtn = findViewById(R.id.seeMoreBtn);

        tvDescription.setText(description); // gán mô tả
        tvDescription.setMaxLines(3); // mặc định 3 dòng

        seeMoreBtn.setOnClickListener(v -> {
            if (seeMoreBtn.getText().equals("Xem thêm ...")) {
                tvDescription.setMaxLines(Integer.MAX_VALUE);
                seeMoreBtn.setText("Thu gọn");
            } else {
                tvDescription.setMaxLines(3);
                seeMoreBtn.setText("Xem thêm ...");
            }
        });

        //favorite

        btnFavorite.setOnClickListener(v -> {
            db.collection("favorites")
                    .document(uid)
                    .collection("movies")
                    .document(idMovie)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            // Nếu đã có → gỡ yêu thích
                            db.collection("favorites")
                                    .document(uid)
                                    .collection("movies")
                                    .document(idMovie)
                                    .delete();

                            btnFavorite.setImageResource(R.drawable.nofavorite);
                        } else {
                            // Thêm vào yêu thích
                            Map<String, Object> movieData = new HashMap<>();
                            movieData.put("title", title);
                            movieData.put("imageUrl", imageUrl);
                            movieData.put("year", year);
                            movieData.put("age", age);
                            movieData.put("rating", rating);
                            movieData.put("genres", genres);

                            db.collection("favorites")
                                    .document(uid)
                                    .collection("movies")
                                    .document(idMovie)
                                    .set(movieData);

                            btnFavorite.setImageResource(R.drawable.favorite);
                        }
                    });
        });


    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
