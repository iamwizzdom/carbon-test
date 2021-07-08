package com.carbon.test.network;

import com.carbon.test.database.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("discover/movie?api_key=c95cfc62578877566659690cef1d6abb")
    Call<MovieList> getMovieList();
}
