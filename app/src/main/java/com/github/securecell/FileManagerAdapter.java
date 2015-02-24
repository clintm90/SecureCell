package com.github.securecell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FileManagerAdapter extends ArrayAdapter<EnumFile>
{
    private final Context context;
    private final List<EnumFile> values;

    public FileManagerAdapter(Context context, List<EnumFile> values)
    {
        super(context, R.layout.model_file, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.model_file, parent, false);
        rowView.setTag(values.get(position));

        ImageView mModelFileIcon = (ImageView)rowView.findViewById(R.id.model_filelist_icon);
        TextView mModelFileTitle = (TextView)rowView.findViewById(R.id.model_filelist_title);
        TextView mModelFileDescription = (TextView)rowView.findViewById(R.id.model_filelist_description);

        mModelFileIcon.setImageDrawable(values.get(position).Icon);
        mModelFileTitle.setText(values.get(position).Title);
        mModelFileDescription.setText(values.get(position).Description);

        return rowView;
    }
}