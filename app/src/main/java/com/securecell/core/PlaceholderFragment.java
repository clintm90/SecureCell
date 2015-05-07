package com.securecell.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private NotificationManager mNotificationManager;

    public PlaceholderFragment()
    {
    }

    public static PlaceholderFragment newInstance(int sectionNumber)
    {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View rootView = null;
        mNotificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        
        switch(getArguments().getInt(ARG_SECTION_NUMBER))
        {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ListView MainContainer = (ListView)rootView.findViewById(R.id.MainContainer);
                
                List<EnumMain> enumMainList = new ArrayList<EnumMain>();
                
                MainListAdapter mainListAdapter = new MainListAdapter(getActivity().getApplicationContext(), enumMainList);

                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), getString(R.string.title_activity_connectivity_center), "Gérer votre accès au réseau", getResources().getDrawable(R.drawable.ic_action_globe), getResources().getDrawable(R.drawable.ic_action_globe_hover)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), getString(R.string.title_activity_task_manager), "Protegez-vous des apps intruisives", getResources().getDrawable(R.drawable.ic_action_database), getResources().getDrawable(R.drawable.ic_action_database_hover)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), getString(R.string.title_activity_backup), "Protegez-vous de la perte de données", getResources().getDrawable(R.drawable.ic_action_cloud), getResources().getDrawable(R.drawable.ic_action_cloud_hover)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), getString(R.string.gps_data), "Configurer l'accès au GPS", getResources().getDrawable(R.drawable.ic_action_location), getResources().getDrawable(R.drawable.ic_action_location_hover)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), getString(R.string.bank_protect), "Protection SSL bancaire", getResources().getDrawable(R.drawable.ic_action_creditcard), getResources().getDrawable(R.drawable.ic_action_creditcard_hover)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), getString(R.string.status_server), "Etat de 151.80.131.143", getResources().getDrawable(R.drawable.ic_action_temperature), getResources().getDrawable(R.drawable.ic_action_temperature_hover)));
                
                MainContainer.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        //((ImageView)view.findViewById(R.id.model_mainlist_icon)).getDrawable().setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);   //.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                        //((ImageView)view.findViewById(R.id.model_mainlist_icon)).setImageDrawable(((EnumMain)view.getTag()).HoverPhoto);
                        
                        Intent ToIntent = null;
                        switch(position)
                        {
                            case 0:
                                ToIntent = new Intent(getActivity().getApplicationContext(), ConnectivityCenter.class);
                                startActivityForResult(ToIntent, 0);
                                break;
                            
                            case 1:
                                ToIntent = new Intent(getActivity().getApplicationContext(), TaskManager.class);
                                startActivityForResult(ToIntent, 0);
                                break;
                            
                            case 2:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Backup.class);
                                startActivityForResult(ToIntent, 0);
                                break;
                            
                            case 3:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                startActivityForResult(ToIntent, 0);
                                break;
                            
                            case 4:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                startActivityForResult(ToIntent, 0);
                                break;
                            
                            case 5:
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                final View mModelStatus = getActivity().getLayoutInflater().inflate(R.layout.alert_status, null);
                                WebView mStatusServiceWebView = (WebView)mModelStatus.findViewById(R.id.statusServiceWebview);
                                final ProgressBar mStatusServiceProgress = (ProgressBar)mModelStatus.findViewById(R.id.statusServiceProgress);
                                mStatusServiceWebView.setWebChromeClient(new WebChromeClient()
                                {
                                    @Override
                                    public void onProgressChanged(WebView view, int progress)
                                    {
                                        mStatusServiceProgress.setProgress(progress);
                                        if (progress == 100)
                                        {
                                            mStatusServiceProgress.setVisibility(View.GONE);
                                        }
                                    }
                                });
                                mStatusServiceWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                                mStatusServiceWebView.getSettings().setAppCacheEnabled(false);
                                mStatusServiceWebView.clearCache(true);
                                mStatusServiceWebView.loadUrl("http://securecellhost.sourceforge.net/ServiceStatus.php");
                                //Initialize.setProxyToWebView(mStatusServiceWebView, Initialize.VPS_DOMAIN, 3128, Initialize.PACKAGE);
                                builder.setView(mModelStatus);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Valider", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                    }
                                });
                                builder.create();
                                builder.show();
                                break;
                            
                            default:
                                //ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                        }
                    }
                });
                
                MainContainer.setAdapter(mainListAdapter);
                mainListAdapter.notifyDataSetChanged();
                return rootView;
            
            case 7:
                rootView = inflater.inflate(R.layout.fragment_about, container, false);
                return rootView;
            
            default:
                rootView = inflater.inflate(R.layout.fragment_about, container, false);
                return rootView;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}