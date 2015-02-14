package com.github.securecell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class EnumMain
{
    public String Title;
    public String Content;
    public Drawable Photo;

    public EnumMain(Context context, String title, Drawable photo)
    {
        Title = title;
        Content = "salut";
        Photo = photo;
    }
}