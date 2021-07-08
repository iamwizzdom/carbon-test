package com.carbon.test.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.carbon.test.common.ApiCallBack;
import com.carbon.test.database.source.MovieDataSource;

import org.jetbrains.annotations.NotNull;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ApiCallBack apiCallBack;
    private final MovieDataSource movieDataSource;

    public ViewModelFactory(ApiCallBack apiCallBack, MovieDataSource movieDataSource) {
        this.apiCallBack = apiCallBack;
        this.movieDataSource = movieDataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            return (T) new MovieViewModel(this.apiCallBack, movieDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
