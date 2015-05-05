package com.securecell.core.service;

import android.util.Log;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

public class ScheduleBackup extends JobService
{
    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        Log.i("---", "ds");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        return false;
    }
}