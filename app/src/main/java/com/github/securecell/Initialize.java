package com.github.securecell;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.github.securecell.service.ProxyServerService;

public class Initialize extends Application
{
    public static String VPS_DOMAIN = "151.80.131.143";
    public static int PROXY_PORT = 9090;
    public static boolean SSL = true;
    public static boolean ServerRunning = false;

    @Override
    public void onCreate()
    {
        super.onCreate();
        
        LaunchProxyServer(this);
    }
    
    public static void LaunchProxyServer(Context context)
    {
        Intent proxyServerService = new Intent(context, ProxyServerService.class);
        
        if(!ServerRunning)
        {
            context.startService(proxyServerService);
        }
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
