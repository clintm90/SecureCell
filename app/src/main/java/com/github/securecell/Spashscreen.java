package com.github.securecell;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Spashscreen extends Activity
{
    Intent MainActivity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spashscreen);

        MainActivity = new Intent(this, Main.class);

        MediaPlayer startupSound = MediaPlayer.create(getApplicationContext(), R.raw.startup);
        startupSound.start();

        final Thread progressThread = new Thread(loader);
        progressThread.setName("loader");
        progressThread.start();
    }

    final Runnable loader = new Runnable()
    {
        public void run()
        {
            ProgressBar pb1 = (ProgressBar) findViewById(R.id.progressBar);
            pb1.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            int jumpTime = 0;
            for (jumpTime = 0; jumpTime <= 100; jumpTime = jumpTime + 2)
            {
                try
                {
                    pb1.setProgress(jumpTime);
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            startActivity(MainActivity);
            finish();
        }
    };
}