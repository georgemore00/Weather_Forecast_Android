package com.example.laboration1.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class is used to check internet connection.
 */
public class NetworkState
{
    /**
     * Takes the application context and uses connectivity manager to check network information.
     * @param context
     * @return true if application is connected to a network.
     */
    public static Boolean isConnected(Context context)
    {
        ConnectivityManager connectionManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectionManager.getActiveNetworkInfo() != null
                && connectionManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
