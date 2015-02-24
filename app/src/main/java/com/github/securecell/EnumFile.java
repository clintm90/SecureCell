package com.github.securecell;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class EnumFile
{
    public String Title;
    public String Description;
    public Drawable Icon;

    public EnumFile(Context context, String title, String description, Drawable icon)
    {
        Title = title;
        Description = description;
        Icon = icon;
    }
}