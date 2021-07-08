package com.carbon.test.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.carbon.test.common.ApiCallBack;
import com.carbon.test.database.model.Movie;
import com.carbon.test.database.model.MovieList;
import com.carbon.test.database.source.MovieDataSource;
import com.carbon.test.network.APIService;
import com.carbon.test.network.RetroInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {

    private final ApiCallBack apiCallBack;
    private final MovieDataSource movieDataSource;
    private final LiveData<List<Movie>> movies;

    public MovieViewModel(ApiCallBack apiCallBack, MovieDataSource movieDataSource) {
        this.apiCallBack = apiCallBack;
        this.movieDataSource = movieDataSource;
        movies = movieDataSource.getMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void insertMovie(Movie movie) {
        this.movieDataSource.insertMovie(movie);
    }

    public void updateMovie(Movie movie) {
        this.movieDataSource.updateMovie(movie);
    }

    public void deleteMovie(Movie movie) {
        this.movieDataSource.deleteMovie(movie);
    }

    public void deleteAllMovies() {
        this.movieDataSource.deleteAllMovies();
    }

    public void getMoviesOnline() {

        deleteAllMovies();

        APIService apiService = RetroInstance.getInstance().create(APIService.class);
        Call<MovieList> call = apiService.getMovieList();
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NotNull Call<MovieList> call, @NotNull Response<MovieList> response) {
                MovieList movies = response.body();
                assert movies != null;
                for (Movie movie: movies.getMovies()) insertMovie(movie);
                if (MovieViewModel.this.apiCallBack != null) MovieViewModel.this.apiCallBack.onComplete();
            }

            @Override
            public void onFailure(@NotNull Call<MovieList> call, @NotNull Throwable t) {
                if (MovieViewModel.this.apiCallBack != null) MovieViewModel.this.apiCallBack.onFailure(t);
            }
        });
    }

}
