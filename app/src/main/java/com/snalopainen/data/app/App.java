package com.snalopainen.data.app;

import android.app.Application;

import com.snalopainen.data.api.Api;

/**
 * Created by snajdan on 2017/1/6.
 */

public class App extends Application {

    private static Api clientApi;

    @Override
    public void onCreate()
    {
        super.onCreate();

        clientApi = new Api();
    }

    public static Api getClientApi()
    {
        return clientApi;
    }
}
