package com.github.securecell;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

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

        if (!ServerRunning)
        {
            context.startService(proxyServerService);
        }
    }

    public static void setMockLocation(Context context, double latitude, double longitude, float accuracy)
    {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener()
        {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            @Override
            public void onProviderEnabled(String provider)
            {
            }

            @Override
            public void onProviderDisabled(String provider)
            {
            }

            @Override
            public void onLocationChanged(Location location)
            {
            }
        });

        lm.addTestProvider(LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(accuracy);

        lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        lm.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

        lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
