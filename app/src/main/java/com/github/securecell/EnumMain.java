package com.github.securecell;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class EnumMain
{
    public String Title;
    public String Description;
    public Drawable Photo;
    public Drawable HoverPhoto;
    public String Status;

    public EnumMain(Context context, String title, String description, Drawable photo, Drawable hover_photo)
    {
        Title = title;
        Description = description;
        Photo = photo;
        HoverPhoto = hover_photo;
    }

    public void SetStatus(String status)
    {
        Status = status;
    }
}