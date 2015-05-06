package com.securecell.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.VpnService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.securecell.core.service.VPNService;

import java.net.ServerSocket;

/*
filter gps autorisation
filter usb
listen 22 port
listen opened port + close port
override gps
filter mcc
filter unknow caller + black list
fingerprint or code
rsa sms
trust network + ip + dns
trust app + decompile dex + permission
Wifi trust
security permission gauge
*/

public class Main extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    private ServerSocket Sock;
    Intent mVPN;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SharedPreferences mPrefsGlobal;
    private SharedPreferences.Editor mStorageGlobal;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mVPN = new Intent(this, VPNService.class);

        mPrefsGlobal = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mStorageGlobal = mPrefsGlobal.edit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Settings.ACTION_ADD_ACCOUNT);
        intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[] {Initialize.PACKAGE});
        startActivity(intent);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number)
    {
    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    public void OpenBrowser(View v)
    {
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("org.mozilla.firefox");
        LaunchIntent.setData(Uri.parse("http://monip.org"));
        Intent intent = new Intent(Main.this, Browser.class);
        startActivity(intent);
    }

    public void Parameters(MenuItem item)
    {
        Intent ParametersActivity = new Intent(this, Parameters.class);
        startActivityForResult(ParametersActivity, 0);
    }

    public void onClick(View v)
    {
        Intent intent = VpnService.prepare(getApplicationContext());
        if (intent != null)
        {
            startActivityForResult(intent, 0);
        }
        else
        {
            onActivityResult(0, RESULT_OK, null);
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent e)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            if(!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            else
            {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }*/

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data.getExtras().getInt("result") == 1)
        {
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }

        if (resultCode == RESULT_OK)
        {
            startService(mVPN);
        }
    }

    @Override
    public void onStart()
    {
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(1))
                .commit();*/
        super.onStart();

    }
    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStop()
    {
        stopService(mVPN);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen())
        {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ShowNavigationDrawer(MenuItem item)
    {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
}