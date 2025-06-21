package com.example.movieapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Adapters.MovieAdapter;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class favoriteactivity extends AppCompatActivity {
    TextView emptyView;
    ImageView btnback;
    RecyclerView movieRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        btnback = findViewById(R.id.btnback);
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        emptyView = findViewById(R.id.emptyView);


        btnback.setOnClickListener(V->finish());

        loadMovies();
    }

    private void loadMovies() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("favorites")
                .document(uid)
                .collection("movies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Movie> movieList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = doc.toObject(Movie.class);
                        if (movie != null) {
                            movie.setId(doc.getId());
                            movieList.add(movie);
                        }
                    }
                    setupRecyclerView(movieList);
                    toggleEmptyView(movieList.isEmpty());
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Load movies error", e));
    }


    private void setupRecyclerView(ArrayList<Movie> movies) {
        MovieAdapter movieAdapter = new MovieAdapter(this, movies, DetailMovieActivity.class,true);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        movieRecyclerView.setAdapter(movieAdapter);
    }

    private void toggleEmptyView(boolean isEmpty) {
        if (isEmpty) {
            movieRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            movieRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }



}
