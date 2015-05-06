package com.securecell.core;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class EnumAccessPoint
{
    public String Title;
    public String Description;
    public Drawable Icon;
    public String Status;

    public EnumAccessPoint(Context context, String title, String description, Drawable icon, String status)
    {
        Title = title;
        Description = description;
        Icon = icon;
        Status = status;
    }
}