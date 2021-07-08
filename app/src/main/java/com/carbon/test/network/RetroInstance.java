package com.carbon.test.network;

import com.carbon.test.constant.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {

    private static Retrofit retrofit;

    public static Retrofit getInstance() {

        if(retrofit == null ) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
