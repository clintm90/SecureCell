package com.securecell.core;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Task extends ActionBarActivity
{
    ApplicationInfo AppInfo;
    String AppName;
    String PackageName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        AppInfo = getIntent().getParcelableExtra("info");
        AppName = getIntent().getStringExtra("name");
        PackageName = getIntent().getStringExtra("package");
        
        setTitle(AppName);
        
        ((TextView)findViewById(R.id.task)).setText("salut");
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                setResult(RESULT_OK, new Intent().putExtra("result", 1));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AlertPackage(MenuItem item)
    {
    }

    public void UninstallPackage(MenuItem item)
    {
        Uri packageURI = Uri.parse("package:" + PackageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        finish();
    }
}