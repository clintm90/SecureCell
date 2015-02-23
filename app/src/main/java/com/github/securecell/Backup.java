package com.github.securecell;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.securecell.service.ScheduleBackup;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class Backup extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        JobScheduler jobScheduler = JobScheduler.getInstance(this);

        // Extras for your job.
        /*PersitableBundle extras = new PersitableBundle();
        extras.putString("key", "value");*/
        
        JobInfo.Builder builder = new JobInfo.Builder(0 /*jobid*/, new ComponentName(this, ScheduleBackup.class));
        //builder.setMinimumLatency(1000);
        //builder.setOverrideDeadline(2000);
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        //builder.setRequiresCharging(false);
        builder.setPeriodic(1000);
        JobInfo job = builder.build();

        jobScheduler.schedule(job);
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
        getMenuInflater().inflate(R.menu.menu_backup, menu);
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