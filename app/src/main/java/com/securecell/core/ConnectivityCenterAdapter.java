package com.securecell.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ConnectivityCenterAdapter extends ArrayAdapter<EnumAccessPoint>
{
    private final Context context;
    private final List<EnumAccessPoint> values;

    public ConnectivityCenterAdapter(Context context, List<EnumAccessPoint> values)
    {
        super(context, R.layout.model_access_point, values);
        this.context = context;
        this.values = values;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.model_access_point, parent, false);
        rowView.setTag(values.get(position));

        ImageView mModelAccessPointIcon = (ImageView)rowView.findViewById(R.id.model_accesspoint_icon);
        ImageView mModelAccessPointState = (ImageView)rowView.findViewById(R.id.model_accesspoint_state);
        TextView mModelAccessPointTitle = (TextView)rowView.findViewById(R.id.model_accesspoint_title);
        TextView mModelAccessPointDescription = (TextView)rowView.findViewById(R.id.model_accesspoint_description);
        TextView mModelAccessPointStatus = (TextView)rowView.findViewById(R.id.model_accesspoint_status);

        mModelAccessPointIcon.setImageDrawable(values.get(position).Icon);
        mModelAccessPointTitle.setText(values.get(position).Title);
        mModelAccessPointDescription.setText(values.get(position).Description);
        mModelAccessPointStatus.setText(values.get(position).Status);

        if(values.get(position).State)
        {
            mModelAccessPointState.setImageDrawable(context.getDrawable(R.drawable.led_green));
        }
        else
        {
            mModelAccessPointState.setImageDrawable(context.getDrawable(R.drawable.led_red));
        }

        return rowView;
    }
}