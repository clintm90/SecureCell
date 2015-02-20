package com.github.securecell;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class TaskManager extends ActionBarActivity
{
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_manager);

        mListView = (ListView) findViewById(R.id.taskManager);

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
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        final List<EnumTask> enumTask = new ArrayList<>();

        final TaskManagerAdapter taskManagerAdapter = new TaskManagerAdapter(getApplicationContext(), enumTask);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                Intent TaskIntent = new Intent(getApplicationContext(), Task.class);
                TaskIntent.putExtra("name", ((EnumTask) view.getTag()).Title);
                TaskIntent.putExtra("package", ((EnumTask) view.getTag()).Package);
                TaskIntent.putExtra("infos", ((EnumTask)view.getTag()).applicationInfo);
                startActivityForResult(TaskIntent, 0);
                
                /*final AlertDialog.Builder taskDialog = new AlertDialog.Builder(TaskManager.this);
                taskDialog.setTitle(((EnumTask) view.getTag()).Title);
                taskDialog.setItems(new String[]{"Lancer l'application", "Voir les permissions", "Nettoyer le cache", "Signaler l'application", "Supprimer l'application"}, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ApplicationInfo appInfo = ((EnumTask) view.getTag()).applicationInfo;
                        switch (which)
                        {
                            case 0: //launch
                                try
                                {
                                    Intent launchActivity = pm.getLaunchIntentForPackage(appInfo.packageName);
                                    startActivity(launchActivity);
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                break;

                            case 1: //permission
                                break;

                            case 2: //flush
                                break;

                            case 3: //add in db
                                break;

                            case 4: //remove
                                Uri packageURI = Uri.parse("package:" + appInfo.packageName);
                                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                                startActivity(uninstallIntent);
                                break;

                            default:
                                break;
                        }
                    }
                });
                taskDialog.create();
                taskDialog.show();*/
            }
        });
        
        AsyncTask<Void, Void, TaskManagerAdapter> MainTask = new AsyncTask<Void, Void, TaskManagerAdapter>()
        {
            @Override
            protected TaskManagerAdapter doInBackground(Void... params)
            {
                for (ApplicationInfo packageInfo : packages)
                {
                    taskManagerAdapter.add(new EnumTask(getApplicationContext(), packageInfo, packageInfo.loadLabel(pm).toString(), packageInfo.packageName, packageInfo.loadIcon(pm)));
                    /*Log.d(TAG, "Installed package :" + packageInfo.packageName);
                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
                    Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));*/
                }
                return taskManagerAdapter;
            }

            @Override
            protected void onPostExecute(TaskManagerAdapter value)
            {
                alertDialog.dismiss();
                mListView.setAdapter(value);
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
