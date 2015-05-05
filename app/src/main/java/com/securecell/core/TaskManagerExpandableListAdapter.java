package com.securecell.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class TaskManagerExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<EnumTask>> _listDataChild;

    public TaskManagerExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<EnumTask>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.model_task, null);
        }

        ImageView mModelTaskIcon = (ImageView) convertView.findViewById(R.id.model_tasklist_icon);
        TextView mModelTaskTitle = (TextView) convertView.findViewById(R.id.model_tasklist_title);
        TextView mModelTaskPackage = (TextView) convertView.findViewById(R.id.model_tasklist_package);
        TextView mModelTaskSize = (TextView) convertView.findViewById(R.id.model_tasklist_size);

        mModelTaskIcon.setImageDrawable(((EnumTask)getChild(groupPosition, childPosition)).Icon);
        mModelTaskTitle.setText(((EnumTask)getChild(groupPosition, childPosition)).Title);
        mModelTaskPackage.setText(((EnumTask)getChild(groupPosition, childPosition)).Package);
        mModelTaskSize.setText(((EnumTask)getChild(groupPosition, childPosition)).Size);

        convertView.setTag(getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.model_groupheader, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textView);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}