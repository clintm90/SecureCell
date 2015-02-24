package com.github.securecell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FileManager extends ActionBarActivity
{
    private ListView MainContainer;
    private List<EnumFile> enumFile = new ArrayList<EnumFile>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_file_manager);
        
        MainContainer = (ListView)findViewById(R.id.fileManager);
        
        FileManagerAdapter fileManagerAdapter = new FileManagerAdapter(this, enumFile);
        
        fileManagerAdapter.add(new EnumFile(this, "salut", "salut", getResources().getDrawable(R.drawable.ic_file_directory), "3,4Mo"));
        
        MainContainer.setAdapter(fileManagerAdapter);

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