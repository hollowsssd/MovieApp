package com.example.movieapp.Fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SearchView;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Activities.DetailMovieActivity;
import com.example.movieapp.Adapters.MovieAdapter;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> allMovies = new ArrayList<>();



    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.movieRecyclerView);


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
        allMovies = movies;
        movieAdapter = new MovieAdapter(getContext(), allMovies, DetailMovieActivity.class,false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(movieAdapter);
    }









}
