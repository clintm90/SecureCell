package com.github.securecell.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.github.securecell.Initialize;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Startup extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread thread, Throwable ex)
            {
                BufferedWriter out;
                try
                {
                    FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/tsxt.txt");
                    out = new BufferedWriter(fileWriter);
                    out.write(ex.getMessage());
                    out.close();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        Initialize.LaunchProxyServer(context);
    }
}