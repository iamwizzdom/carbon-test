package com.carbon.test.database.source;

import androidx.lifecycle.LiveData;

import com.carbon.test.database.access.MovieDao;
import com.carbon.test.database.model.Movie;

import java.util.List;

public class MovieDataSource {

    private final MovieDao movieDao;

    public MovieDataSource(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public LiveData<List<Movie>> getMovies() {
        return this.movieDao.getAllMovies();
    }

    public void insertMovie(Movie movie) {
        new Thread(() -> MovieDataSource.this.movieDao.insertMovie(movie)).start();
    }

    public void updateMovie(Movie movie) {
        new Thread(() -> MovieDataSource.this.movieDao.updateMovie(movie)).start();
    }

    public void deleteMovie(Movie movie) {
        new Thread(() -> MovieDataSource.this.movieDao.deleteMovie(movie)).start();
    }

    public void deleteAllMovies() {
        new Thread(MovieDataSource.this.movieDao::deleteAllMovies).start();
    }
}
