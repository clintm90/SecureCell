package com.github.securecell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TaskManagerAdapter extends ArrayAdapter<EnumTask>
{
    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
    private final Context context;
    private final List<EnumTask> values;

    public TaskManagerAdapter(Context context, List<EnumTask> values)
    {
        super(context, R.layout.model_task, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.model_task, parent, false);
        rowView.setTag(values.get(position));

        ImageView mModelTaskIcon = (ImageView)rowView.findViewById(R.id.model_tasklist_icon);
        TextView mModelTaskTitle = (TextView)rowView.findViewById(R.id.model_tasklist_title);
        TextView mModelTaskPackage = (TextView)rowView.findViewById(R.id.model_tasklist_package);

        mModelTaskIcon.setImageDrawable(values.get(position).Icon);
        mModelTaskTitle.setText(values.get(position).Title);
        mModelTaskPackage.setText(values.get(position).Package);

        return rowView;
    }
}