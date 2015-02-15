package com.github.securecell;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class EnumMain
{
    public String Title;
    public String Description;
    public Drawable Photo;

    public EnumMain(Context context, String title, String description, Drawable photo)
    {
        Title = title;
        Description = description;
        Photo = photo;
    }
}