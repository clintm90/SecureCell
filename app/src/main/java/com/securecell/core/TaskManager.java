package com.securecell.core;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaskManager extends ActionBarActivity
{
    private ExpandableListView  mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_manager);

        ActivityManager activityManager = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        mListView = (ExpandableListView) findViewById(R.id.taskManager);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        final ProgressDialog alertDialog = ProgressDialog.show(this, "", getString(R.string.loading));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                setResult(RESULT_OK, new Intent().putExtra("result", 1));
                finish();
            }
        });
        alertDialog.show();

        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> allPackages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        final List<ActivityManager.RunningAppProcessInfo> runningPackages = activityManager.getRunningAppProcesses();
        final List<String> otherPackages = new ArrayList<>();
        final List<EnumTask> mRunningApps = new ArrayList<>();
        final List<EnumTask> mOtherApps = new ArrayList<>();

        final List<String> listDataHeader = new ArrayList<String>();
        final HashMap<String, List<EnumTask>> listDataChild = new HashMap<String, List<EnumTask>>();

        final TaskManagerExpandableListAdapter mTaskExpandableListAdapter = new TaskManagerExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Intent TaskIntent = new Intent(getApplicationContext(), Task.class);
                TaskIntent.putExtra("name", ((EnumTask) v.getTag()).Title);
                TaskIntent.putExtra("package", ((EnumTask) v.getTag()).Package);
                TaskIntent.putExtra("infos", ((EnumTask) v.getTag()).applicationInfo);
                startActivityForResult(TaskIntent, 0);
                return false;
            }
        });

        listDataHeader.add(getString(R.string.running_apps));
        listDataHeader.add(getString(R.string.others_apps));

        AsyncTask<Void, Void, TaskManagerExpandableListAdapter> MainTask = new AsyncTask<Void, Void, TaskManagerExpandableListAdapter>()
        {
            @Override
            protected TaskManagerExpandableListAdapter doInBackground(Void... params)
            {
                for(ApplicationInfo process : allPackages)
                {
                    otherPackages.add(process.packageName);
                }

                for(ActivityManager.RunningAppProcessInfo process : runningPackages)
                {
                    try
                    {
                        ApplicationInfo appInfo = pm.getApplicationInfo(process.processName, 0);
                        otherPackages.remove(appInfo.packageName);
                        mRunningApps.add(new EnumTask(getApplicationContext(), appInfo, appInfo.loadLabel(pm).toString(), appInfo.processName, appInfo.loadIcon(pm), Formatter.formatShortFileSize(getApplicationContext(), new File(appInfo.sourceDir).length())));
                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

                for(String process : otherPackages)
                {
                    try
                    {
                        ApplicationInfo appInfo = pm.getApplicationInfo(process, 0);
                        mOtherApps.add(new EnumTask(getApplicationContext(), appInfo, appInfo.loadLabel(pm).toString(), appInfo.processName, appInfo.loadIcon(pm), Formatter.formatShortFileSize(getApplicationContext(), new File(appInfo.sourceDir).length())));
                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

                listDataChild.put(listDataHeader.get(0), mRunningApps);
                listDataChild.put(listDataHeader.get(1), mOtherApps);
                return mTaskExpandableListAdapter;
            }
            
            @Override
            protected void onPostExecute(TaskManagerExpandableListAdapter input)
            {
                alertDialog.dismiss();
                mListView.setAdapter(input);
                try
                {
                    mListView.expandGroup(0);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    mListView.expandGroup(1);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        MainTask.execute();
    }

    private boolean isSystemPackage(PackageInfo pkgInfo)
    {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        super.onBackPressed();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data.getExtras().getInt("result") == 1)
        {
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_manager, menu);
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