package com.carbon.test.common;

import android.content.Context;

import com.carbon.test.database.MovieDatabase;
import com.carbon.test.database.source.MovieDataSource;
import com.carbon.test.viewmodel.ViewModelFactory;

public class Injection {

    public static MovieDataSource provideMovieDataSource(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context);
        return new MovieDataSource(database.movieDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context, ApiCallBack apiCallBack) {
        MovieDataSource dataSource = provideMovieDataSource(context);
        return new ViewModelFactory(apiCallBack, dataSource);
    }
}
