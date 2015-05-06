package com.securecell.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ConnectivityCenter extends ActionBarActivity
{
    private ListView MainContainer;
    private List<EnumAccessPoint> enumAccessPoint = new ArrayList<EnumAccessPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connectivity_center);

        MainContainer = (ListView) findViewById(R.id.fileManager);

        ConnectivityCenterAdapter fileManagerAdapter = new ConnectivityCenterAdapter(this, enumAccessPoint);

        fileManagerAdapter.add(new EnumAccessPoint(getApplicationContext(), "NEUF_A400", "Sécurisé par WPA2", null, "2"));

        MainContainer.setAdapter(fileManagerAdapter);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
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
        getMenuInflater().inflate(R.menu.menu_connectivity_center, menu);
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
