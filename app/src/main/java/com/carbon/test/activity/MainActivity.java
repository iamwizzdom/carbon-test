package com.carbon.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.carbon.test.R;
import com.carbon.test.adapter.MoviesAdapter;
import com.carbon.test.common.ApiCallBack;
import com.carbon.test.common.Injection;
import com.carbon.test.common.Validator;
import com.carbon.test.common.interfaces.RecyclerViewItem;
import com.carbon.test.viewmodel.MovieViewModel;
import com.carbon.test.viewmodel.ViewModelFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstTime = true;
    private MovieViewModel movieViewModel;
    private MoviesAdapter moviesAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final ArrayList<RecyclerViewItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Validator.isNetworkConnected(MainActivity.this)) {
                setRefreshing(true);
                movieViewModel.getMoviesOnline();
            } else {
                setRefreshing(false);
                setLoading(false, moviesAdapter.hasItem());
                toastMessage("No internet connection");
            }
        });

        RecyclerView recyclerView = findViewById(R.id.movie_list);

        moviesAdapter = new MoviesAdapter(recyclerView, this, items);
        recyclerView.setAdapter(moviesAdapter);

        ViewModelFactory factory = Injection.provideViewModelFactory(this, new ApiCallBack() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onFailure(Throwable t) {
                setLoading(false, moviesAdapter.hasItem());
            }
        });

        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                items.addAll(movies);
                isFirstTime = false;
                setLoading(false, true);
            } else if (isFirstTime) {
                isFirstTime = false;
                if (Validator.isNetworkConnected(MainActivity.this)) movieViewModel.getMoviesOnline();
                else {
                    setLoading(false, moviesAdapter.hasItem());
                    toastMessage("No internet connection");
                }
            } else {
                setLoading(isRefreshing(), false);
            }
        });

        items.add(() -> RecyclerViewItem.VIEW_TYPE_LOADING);
        moviesAdapter.notifyDataSetChanged();

    }

    public void setLoading(boolean isLoading, boolean hasContent) {

        if (isLoading) {

            if (hasContent) items.add(() -> RecyclerViewItem.VIEW_TYPE_LOADING_MORE);
            else {
                moviesAdapter.removeAllViewItems();
                items.add(() -> RecyclerViewItem.VIEW_TYPE_LOADING);
            }

        } else {

            if (hasContent) moviesAdapter.removeAllViewItemExcept(RecyclerViewItem.VIEW_TYPE_ITEM);
            else {
                moviesAdapter.removeAllViewItemExcept(RecyclerViewItem.VIEW_TYPE_HEADER);
                items.add(() -> RecyclerViewItem.VIEW_TYPE_NO_CONTENT);
            }
        }

        moviesAdapter.notifyDataSetChanged();

        if (isRefreshing()) setRefreshing(false);
    }

    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
        moviesAdapter.setPermitLoadMore(refreshing && !moviesAdapter.isPermitLoadMore());
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}