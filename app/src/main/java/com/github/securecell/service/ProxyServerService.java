package com.github.securecell.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.github.securecell.proxy.ProxyServer;

public class ProxyServerService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Thread SockThread = new Thread(new ProxyServer());
        SockThread.setName("ProxyServer");
        SockThread.setPriority(Thread.MAX_PRIORITY);
        SockThread.setDaemon(true);
        SockThread.start();

        return START_STICKY;
    }
}
