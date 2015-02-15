package com.github.securecell;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PlaceholderFragment newInstance(int sectionNumber)
    {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = null;
        
        switch(getArguments().getInt(ARG_SECTION_NUMBER))
        {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ListView MainContainer = (ListView)rootView.findViewById(R.id.MainContainer);
                
                List<EnumMain> enumMainList = new ArrayList<EnumMain>();
                
                MainListAdapter mainListAdapter = new MainListAdapter(getActivity().getApplicationContext(), enumMainList);

                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "Centre des Connexions", "Gérer votre accès au réseau", getResources().getDrawable(R.drawable.ic_action_globe)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "Gestionnaire des Tâches", "Protegez-vous des apps intruisives", getResources().getDrawable(R.drawable.ic_action_database)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "Sauvegarde en Ligne", "Protegez-vous de la perte de données", getResources().getDrawable(R.drawable.ic_action_cloud)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "Données de Localisation", "Configurer l'accès au GPS", getResources().getDrawable(R.drawable.ic_action_location)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "Protection Bancaire", "Protection SSL bancaire", getResources().getDrawable(R.drawable.ic_action_creditcard)));
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "Etat du Service", "Etat de 151.80.131.143", getResources().getDrawable(R.drawable.ic_action_temperature)));
                
                MainContainer.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        ((ImageView)view.findViewById(R.id.model_mainlist_icon)).getDrawable().setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);   //.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                        
                        Intent ToIntent = null;
                        switch(position)
                        {
                            case 0:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                            
                            case 1:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                            
                            case 2:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                            
                            case 3:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                            
                            case 4:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                            
                            case 5:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                            
                            default:
                                ToIntent = new Intent(getActivity().getApplicationContext(), Browser.class);
                                break;
                        }
                        startActivityForResult(ToIntent, 0);
                    }
                });
                
                MainContainer.setAdapter(mainListAdapter);
                return rootView;
            
            default:
                rootView = inflater.inflate(R.layout.fragment_about, container, false);
                return rootView;
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}