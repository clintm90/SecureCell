package com.github.securecell.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.securecell.Initialize;

public class Startup extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        /*Intent proxyServerService = new Intent(context, ProxyServerService.class);
        context.startService(proxyServerService);*/
        Initialize.LaunchProxyServer(context);
    }
}