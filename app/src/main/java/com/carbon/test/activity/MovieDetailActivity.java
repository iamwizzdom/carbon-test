package com.carbon.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carbon.test.R;
import com.carbon.test.database.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent i = getIntent();
        Movie movie = (Movie) i.getSerializableExtra("movie");

        Toolbar toolbar = findViewById(R.id.toolbar);

        ImageView ivMoviePoster = findViewById(R.id.movie_poster);
        TextView tvReleaseDate = findViewById(R.id.release_date);
        TextView tvVote = findViewById(R.id.vote);
        TextView tvOverview = findViewById(R.id.overview);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        Glide.with(this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_image_placeholder)
                .into(ivMoviePoster);

        toolbar.setTitle(movie.getTitle());
        tvReleaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
        tvVote.setText(String.valueOf(movie.getVote()));
        ratingBar.setRating(movie.getVote() / 2);
        tvOverview.setText(movie.getOverview());
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}