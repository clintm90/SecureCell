package com.github.securecell.receiver;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.github.securecell.Main;
import com.github.securecell.R;

import java.util.List;

public class PackageInstallation extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Uri mSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        String mAppName = null;
        
        String[] a = intent.getData().toString().split(":");
        String packageName = a[a.length-1];
        List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++)
        {
            PackageInfo packageInfo = packageInfoList.get(i);
            if(packageInfo.packageName.equals(packageName))
            {
                mAppName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                String mAppVersion = packageInfo.versionName;
                Drawable mAppIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                String[] mPermissions = packageInfo.requestedPermissions;
            }
        }
        
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        mBuilder.setSmallIcon(R.drawable.ic_stat_appinstalled);
        mBuilder.setContentTitle(mAppName); //context.getString(R.string.app_name)
        mBuilder.setContentText(context.getString(R.string.appinstalledwouldinternet));
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setTicker(context.getString(R.string.appinstalled));
        mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Main.class), 0));
        mBuilder.addAction(R.drawable.ic_action_yes, context.getResources().getString(R.string.yes), PendingIntent.getActivity(context, 0, new Intent(context, Main.class), 0));
        mBuilder.addAction(R.drawable.ic_action_no, context.getResources().getString(R.string.no), PendingIntent.getActivity(context, Activity.RESULT_OK, new Intent(context, Main.class), 0));
        //mBuilder.setSound(mSoundUri);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED") || intent.getAction().equals("android.intent.action.PACKAGE_INSTALL"))
        {
            mNotificationManager.notify(mAppName.hashCode(), mBuilder.build());
        }
    }

}