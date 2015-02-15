package com.github.securecell;

import android.app.Application;

public class Initialize extends Application
{
    String SERVER_DOMAIN = "securecell.ddns.net";
    boolean SSL = true;
    
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
