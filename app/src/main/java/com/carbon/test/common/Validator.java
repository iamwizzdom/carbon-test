package com.carbon.test.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;

public class Validator {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = null;
        if (connectivityManager != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        }
        return (networkCapabilities != null && networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET));
    }

}
