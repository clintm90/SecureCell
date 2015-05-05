package com.securecell.core;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

public class Spashscreen extends Activity
{
    Intent MainActivity;

    private Cipher mCipher;
    private KeyPairGenerator mKeyPairGenerator;
    private KeyFactory mKeyFactory;
    private KeyPair mKP;
    private SharedPreferences mPrefsGlobal;
    private SharedPreferences.Editor mStorageGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mPrefsGlobal = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setContentView(R.layout.activity_spashscreen);

        MainActivity = new Intent(this, Main.class);

        MediaPlayer startupSound = MediaPlayer.create(getApplicationContext(), R.raw.startup);
        startupSound.start();

        final Thread ProgressThread = new Thread(Loader);
        final Thread InitThread = new Thread(Init);

        ProgressThread.setName("ProgressThread");
        InitThread.setName("InitThread");

        ProgressThread.start();
        InitThread.start();
    }

    final Runnable Init = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                mCipher = Cipher.getInstance("RSA");
                mKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
                mKeyPairGenerator.initialize(512);
                //mKeyFactory = KeyFactory.getInstance("RSA");
                mKP = mKeyPairGenerator.generateKeyPair();
                mStorageGlobal = mPrefsGlobal.edit();
                mStorageGlobal.putString("RSAPublicKey", mKP.getPublic().getEncoded().toString());
                mStorageGlobal.putString("RSAPrivateKey", mKP.getPrivate().getEncoded().toString());
                mStorageGlobal.apply();
            }
            catch (Exception exception)
            {
                mKeyPairGenerator = null;
            }
        }
    };

    final Runnable Loader = new Runnable()
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