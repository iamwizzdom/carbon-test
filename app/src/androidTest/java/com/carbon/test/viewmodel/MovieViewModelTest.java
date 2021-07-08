package com.carbon.test.viewmodel;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.carbon.test.database.MovieDatabase;
import com.carbon.test.database.model.Movie;
import com.carbon.test.database.source.MovieDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.carbon.test.LiveDataTestUtil.getOrAwaitValue;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class MovieViewModelTest {

    private MovieViewModel viewModel;
    private MovieDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {

        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class)
                .allowMainThreadQueries()
                .build();
        MovieDataSource dataSource = new MovieDataSource(database.movieDao());;
        viewModel = new MovieViewModel(null, dataSource);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testMovieViewModel() throws InterruptedException {

        Movie movie = new Movie();
        movie.setMovieID(1);
        movie.setTitle("Luca");
        movie.setReleaseDate("2020-05-23");
        movie.setOverview("Interesting movie");
        movie.setPosterPath("/luca.png");
        viewModel.insertMovie(movie);

        final Movie[] newMovie = {null};
        getOrAwaitValue(viewModel.getMovies()).iterator().forEachRemaining(movie1 -> {
            if (movie1.getMovieID() == 1) newMovie[0] = movie1;
        });

        assertThat(newMovie[0] != null).isTrue();
    }
}