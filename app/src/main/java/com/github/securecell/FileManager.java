package com.github.securecell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class FileManager extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_file_manager);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public void NewFile(MenuItem item)
    {
    }

    public void NewDirectory(MenuItem item)
    {
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
        getMenuInflater().inflate(R.menu.menu_file_manager, menu);
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
}