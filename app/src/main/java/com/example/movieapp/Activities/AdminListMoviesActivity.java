package com.example.movieapp.Activities;



import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.gestures.snapping.SnapPosition;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Adapters.MovieAdapter;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminListMoviesActivity extends AppCompatActivity {

    Button btnthem;
    ImageButton btnBack1;
    SearchView searchView;
    RecyclerView recyclerView;


    private MovieAdapter movieAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_movies);

        btnthem=findViewById(R.id.btnthem);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.movieRecyclerView);
        btnBack1 = findViewById(R.id.btnBack1);

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminListMoviesActivity.this, AdminAddMovieActivity.class);
                startActivity(intent);

            }
        });
        btnBack1.setOnClickListener(v -> finish());


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (movieAdapter != null) {
                    movieAdapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (movieAdapter != null) {
                    movieAdapter.filter(newText);
                }
                return false;
            }
        });


        loadMovies();





    }

    private void loadMovies() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("movies").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Movie> movieList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = doc.toObject( Movie.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    }
                    setupRecyclerView(movieList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Load movies error", e));
    }
    private void setupRecyclerView(ArrayList<Movie> movies) {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(this, movies, AdminDetailMoviesActivity.class,false);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(movieAdapter);
        } else {
            movieAdapter.updateData(movies); // Quan trọng
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }
}
