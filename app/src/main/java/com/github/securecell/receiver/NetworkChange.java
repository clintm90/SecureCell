package com.github.securecell.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.github.securecell.Main;

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
                    Intent MainActivity = new Intent(context, Main.class);
                    MainActivity.putExtra("WifiInfo", wifiInfo);
                    MainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(MainActivity);
                    Toast.makeText(context.getApplicationContext(), wifiInfo.getMacAddress(), Toast.LENGTH_LONG).show();
                }
                else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                {
                    Intent MainActivity = new Intent(context, Main.class);
                    MainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(MainActivity);
                }
            }
            else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE))
            {
            }
        }
    }
}