package com.example.movieapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.movieapp.Activities.DetailMovieActivity;
import com.example.movieapp.Activities.MediaActivity;
import com.example.movieapp.Adapters.MovieAdapter;
import com.example.movieapp.Adapters.SliderAdapter;
import com.example.movieapp.Domain.SliderItems;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager2;
    private Button btnPlayNow;
    private TextView title, genre;
    private RecyclerView topMoviesRecycler;
    private final Handler sliderHandel = new Handler();

    private Runnable sliderRunable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.bannerImage);
        btnPlayNow = view.findViewById(R.id.btnPlayNow);
        title = view.findViewById(R.id.Title);
        genre = view.findViewById(R.id.Genres);
        topMoviesRecycler = view.findViewById(R.id.topMoviesRecycler);

        banner();

        loadMovies();
    }

    private void banner() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.drstrange,"Dr Strange 2", "Action, Superhero, Science Fiction","3a2c3c6ad53c6b93e5d7157d62e74967"));
        sliderItems.add(new SliderItems(R.drawable.harry,"Harry Potter", "Action, adventure, Fantasy","1385c31b5163cee2acc1049e1b4491b2"));
        sliderItems.add(new SliderItems(R.drawable.all_quiet_on_the_western_front, "All Quiet On The Western Front","Epic, War epic, History","eeaf404b5663736fe1873cc018ef97a7"));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.setClickable(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (position >= sliderItems.size()) return;
                SliderItems current = sliderItems.get(position);
                title.setText(current.getTitle());
                genre.setText(current.getGenre());
                btnPlayNow.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), MediaActivity.class);
                    intent.putExtra("videoUrl", current.getVideoUrl());
                    intent.putExtra("title", current.getTitle());
                    startActivity(intent);
                });

                sliderHandel.removeCallbacks(sliderRunable);
                sliderHandel.postDelayed(sliderRunable, 3000);
            }
        });
    }

    private void loadMovies() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("movies").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Movie> movieList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = doc.toObject(Movie.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    }
                    setupRecyclerView(movieList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Load movies error", e));
    }

    private void setupRecyclerView(ArrayList<Movie> movies) {
        MovieAdapter adapter = new MovieAdapter(getContext(), movies, DetailMovieActivity.class,false);
        topMoviesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topMoviesRecycler.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandel.removeCallbacks(sliderRunable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandel.postDelayed(sliderRunable, 3000);
    }
}
