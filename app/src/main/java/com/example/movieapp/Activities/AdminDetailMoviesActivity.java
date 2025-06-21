package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDetailMoviesActivity extends AppCompatActivity {
    TextView etTitle,etRating,etYear,etAge,etDescription, etImageUrl, etVideoUrl,etGenres;
    Button btnSubmit,btnDelete;
    ImageButton btnBack1;
    FirebaseFirestore db;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_movies);

        etTitle = findViewById(R.id.etTitle);
        etYear = findViewById(R.id.etYear);
        etRating =findViewById(R.id.etRating);
        etAge=findViewById(R.id.etAge);
        etDescription=findViewById(R.id.etDescription);
        etGenres=findViewById(R.id.etGenres);
        etImageUrl=findViewById(R.id.etImageUrl);
        etVideoUrl=findViewById(R.id.etVideoUrl);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnDelete=findViewById(R.id.btnDelete);
        db= FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);
        btnBack1=findViewById(R.id.btnBack1);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int year = intent.getIntExtra("year", 0);
        int age = intent.getIntExtra("age", 0);
        ArrayList<String> genresList = intent.getStringArrayListExtra("genres");
        double rating = intent.getDoubleExtra("rating", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");
        String videoUrl = intent.getStringExtra("videoUrl");


        etTitle.setText(title);
        etDescription.setText(description);
        etYear.setText(String.valueOf(year));
        etAge.setText(String.valueOf(age));
        etRating.setText(String.valueOf(rating));
        etGenres.setText(genresList != null ? String.join(", ", genresList) : "");
        etImageUrl.setText(imageUrl);
        etVideoUrl.setText(videoUrl);

        btnBack1.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!check()) {
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                Map<String, Object> updatedMovie = new HashMap<>();
                updatedMovie.put("title", etTitle.getText().toString());
                updatedMovie.put("description", etDescription.getText().toString());
                updatedMovie.put("year", Integer.parseInt(etYear.getText().toString()));
                updatedMovie.put("age", Integer.parseInt(etAge.getText().toString()));
                updatedMovie.put("rating", Double.parseDouble(etRating.getText().toString()));
                updatedMovie.put("imageUrl", etImageUrl.getText().toString());
                updatedMovie.put("videoUrl", etVideoUrl.getText().toString());

                String genresText = etGenres.getText().toString();
                List<String> genresList = Arrays.asList(genresText.split("\\s*,\\s*"));
                updatedMovie.put("genres", genresList);

                db.collection("movies").document(id)
                        .update(updatedMovie)
                        .addOnSuccessListener(unused -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminDetailMoviesActivity.this, "Đã cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish(); // đóng màn
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminDetailMoviesActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdminDetailMoviesActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa phim này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            progressBar.setVisibility(View.VISIBLE);
                            db.collection("movies").document(id)
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AdminDetailMoviesActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AdminDetailMoviesActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });


    }



    private boolean check() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();
        String genres = etGenres.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Vui lòng nhập tên phim", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(yearStr)) {
            Toast.makeText(this, "Vui lòng nhập năm", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Vui lòng nhập độ tuổi", Toast.LENGTH_SHORT).show();
            return false;
        }

        int age = Integer.parseInt(ageStr);
        if (age < 1 || age > 20) {
            Toast.makeText(this, "Độ tuổi phải từ 1 đến 20", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(genres)) {
            Toast.makeText(this, "Vui lòng nhập thể loại", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(ratingStr)) {
            Toast.makeText(this, "Vui lòng nhập xếp hạng", Toast.LENGTH_SHORT).show();
            return false;
        }

        double rating = Double.parseDouble(ratingStr);
        if (rating < 0 || rating > 10) {
            Toast.makeText(this, "Xếp hạng phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
