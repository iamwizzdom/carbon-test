package com.carbon.test.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.carbon.test.database.access.MovieDao;
import com.carbon.test.database.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;

    public abstract MovieDao movieDao();

    public static synchronized MovieDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return instance;
    }
}
