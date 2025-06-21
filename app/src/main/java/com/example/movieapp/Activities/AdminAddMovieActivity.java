package com.example.movieapp.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminAddMovieActivity extends AppCompatActivity {
    EditText etTitle,etDescription,etYear,etAge,etGenres,etRating,etImageUrl,etVideoUrl;

    Button btnSubmit;
    ImageButton btnBack1;

    FirebaseFirestore db;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addmovie);

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        etYear=findViewById(R.id.etYear);
        etAge=findViewById(R.id.etAge);
        etGenres=findViewById(R.id.etGenres);
        etRating=findViewById(R.id.etRating);
        etImageUrl=findViewById(R.id.etImageUrl);
        etVideoUrl=findViewById(R.id.etVideoUrl);
        btnBack1=findViewById(R.id.btnBack1);
        progressBar =findViewById(R.id.progressBar);
        btnSubmit=findViewById(R.id.btnSubmit);


        btnBack1.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String yearStr = etYear.getText().toString().trim();
                String ageStr = etAge.getText().toString().trim();
                String genres = etGenres.getText().toString().trim();
                String ratingStr = etRating.getText().toString().trim();
                String imageUrl = etImageUrl.getText().toString().trim();
                String videoUrl = etVideoUrl.getText().toString().trim();

                // Kiểm tra các trường bắt buộc
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(AdminAddMovieActivity.this, "Vui lòng nhập tên phim", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(AdminAddMovieActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(yearStr)) {
                    Toast.makeText(AdminAddMovieActivity.this, "Vui lòng nhập năm", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(ageStr)) {
                    Toast.makeText(AdminAddMovieActivity.this, "Vui lòng nhập độ tuổi", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age = Integer.parseInt(ageStr);
                if (age < 1 || age > 20) {
                    Toast.makeText(AdminAddMovieActivity.this, "Độ tuổi phải từ 1 đến 20", Toast.LENGTH_SHORT).show();
                    return;
                }
                int year= Integer.parseInt(yearStr);


                if (TextUtils.isEmpty(genres)) {
                    Toast.makeText(AdminAddMovieActivity.this, "Vui lòng nhập thể loại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(ratingStr)) {
                    Toast.makeText(AdminAddMovieActivity.this, "Vui lòng nhập xếp hạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                double rating = Double.parseDouble(ratingStr);
                if (rating < 0 || rating > 10) {
                    Toast.makeText(AdminAddMovieActivity.this, "Xếp hạng phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    return;
                }



                AddMovie(title,description,year,age,genres,rating,imageUrl,videoUrl);

            }
        });

    }
    public void AddMovie(String title, String description, int year, int age, String genres, Double rating, String imageUrl, String videoUrl) {
        progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        // Tạo ID ngẫu nhiên
        String id = db.collection("movies").document().getId();

        // Parse genres từ String sang List<String>
        ArrayList<String> genresList = new ArrayList<>(Arrays.asList(genres.split("\\s*,\\s*"))); // Cách nhau bằng dấu phẩy

        // Tạo object Movie
        Movie movie = new Movie(id, title, description, year, age, genresList, rating, imageUrl, videoUrl);

        // Gửi lên Firestore
        db.collection("movies").document(id).set(movie)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Thêm phim thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi khi thêm phim: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
