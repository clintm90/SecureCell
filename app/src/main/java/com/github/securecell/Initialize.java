package com.github.securecell;

import android.app.Application;

public class Initialize extends Application
{
    public static String SERVER_DOMAIN = "151.80.131.143";
    public static boolean SSL = true;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
