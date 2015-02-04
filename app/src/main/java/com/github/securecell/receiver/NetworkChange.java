package com.github.securecell.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetworkChange extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
        {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            
            if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            {
                if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                {
                    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    Toast.makeText(context.getApplicationContext(), wifiInfo.getSSID(), Toast.LENGTH_LONG).show();
                }
                else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                {
                    Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            }
            else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE))
            {
            }
        }
    }
}