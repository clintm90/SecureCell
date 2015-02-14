package com.github.securecell;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                
                mainListAdapter.add(new EnumMain(getActivity().getApplicationContext(), "salut", null));
                
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