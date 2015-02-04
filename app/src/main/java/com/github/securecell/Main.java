package com.github.securecell;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.github.securecell.service.VPNService;

public class Main extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
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

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        
        setContentView(R.layout.activity_main);

        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("Navigation Drawer");
            setSupportActionBar(mToolbar);
        }
        mToolbar.setNavigationIcon(R.drawable.ic_launcher);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);*/

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout));
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            startService(mVPN);
        }
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
        switch(item.getItemId())
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