package com.example.movieapp.Adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.Activities.DetailMovieActivity;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movieList;
    private ArrayList<Movie> originalList;
    private Class<?> targetActivity;

    private boolean isFavoriteMode; // true nếu đang ở màn yêu thích

    // Constructor có thêm biến xác định chế độ yêu thích
    public MovieAdapter(Context context, ArrayList<Movie> movieList, Class<?> targetActivity, boolean isFavoriteMode) {
        this.context = context;
        this.movieList = new ArrayList<>(movieList);
        this.originalList = new ArrayList<>(movieList);
        this.targetActivity = targetActivity;
        this.isFavoriteMode = isFavoriteMode;
    }

    // Trả về kiểu View tùy theo chế độ yêu thích hay không
    @Override
    public int getItemViewType(int position) {
        return isFavoriteMode ? 1 : 0;
    }

    // Inflate layout theo viewType
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_movie_favorite, parent, false);
            return new FavoriteMovieHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_movie, parent, false);
            return new MovieHolder(view);
        }
    }

    // Gán dữ liệu cho từng kiểu ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(30));

        if (holder instanceof MovieHolder) {
            MovieHolder h = (MovieHolder) holder;
            h.title.setText(movie.getTitle());

            Glide.with(context)
                    .load(movie.getImageUrl())
                    .apply(requestOptions)
                    .into(h.imagePoster);

        } else if (holder instanceof FavoriteMovieHolder) {
            FavoriteMovieHolder h = (FavoriteMovieHolder) holder;
            h.tvTitle.setText(movie.getTitle());
            h.tvGenres.setText(TextUtils.join(", ", movie.getGenres()));
            h.tvRating.setText(String.valueOf(movie.getRating()));
            h.tvAge.setText(String.valueOf(movie.getAge()));
            h.tvYear.setText(String.valueOf(movie.getYear()));

            Glide.with(context)
                    .load(movie.getImageUrl())
                    .apply(requestOptions)
                    .into(h.imagePoster);
        }

        // Cả 2 chế độ đều xử lý click giống nhau
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, targetActivity);
            intent.putExtra("id", movie.id);
            Log.d("TAG", "id movie " + movie.id);  // nên true

            intent.putExtra("title", movie.title);
            intent.putExtra("description", movie.description);
            intent.putExtra("year", movie.year);
            intent.putExtra("age", movie.age);
            intent.putStringArrayListExtra("genres", new ArrayList<>(movie.genres));
            intent.putExtra("rating", movie.rating);
            intent.putExtra("imageUrl", movie.imageUrl);
            intent.putExtra("videoUrl", movie.videoUrl);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // ViewHolder mặc định
    public static class MovieHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imagePoster;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            imagePoster = itemView.findViewById(R.id.poster);
        }
    }

    // ViewHolder cho layout yêu thích
    public static class FavoriteMovieHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvGenres, tvRating, tvAge, tvYear;
        ImageView imagePoster;

        public FavoriteMovieHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGenres = itemView.findViewById(R.id.tvGenres);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvYear = itemView.findViewById(R.id.tvYear);
            imagePoster = itemView.findViewById(R.id.poster);
        }
    }

    // Hàm lọc phim theo từ khóa
    public void filter(String text) {
        movieList.clear();
        if (text.isEmpty()) {
            movieList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Movie movie : originalList) {
                if (movie.getTitle().toLowerCase().contains(text)) {
                    movieList.add(movie);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Cập nhật danh sách phim
    public void updateData(ArrayList<Movie> newMovies) {
        movieList.clear();
        movieList.addAll(newMovies);

        originalList.clear();
        originalList.addAll(newMovies);

        notifyDataSetChanged();
    }
}
