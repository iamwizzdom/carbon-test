package com.carbon.test.database.access;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.carbon.test.database.model.Movie;

import java.util.List;

@androidx.room.Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie model);


    @Update
    void updateMovie(Movie model);


    @Delete
    void deleteMovie(Movie model);


    @Query("DELETE FROM movies")
    void deleteAllMovies();


    @Query("SELECT * FROM movies ORDER BY title ASC")
    LiveData<List<Movie>> getAllMovies();

}
